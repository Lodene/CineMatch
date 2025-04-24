from fastapi import FastAPI
import stomp
import json
import uuid
from pydantic import BaseModel
from typing import Dict
import json
import re
from dataclasses import dataclass
from typing import Optional, List
from core.utils.decorators import to_time
from core.utils.utils_inference import load_inference_data, recommend_movies

app = FastAPI(title="FastAPI ActiveMQ Integration")

# load ai model => 
df_full, df_enriched, model = load_inference_data()

class MovieDto(BaseModel):
    id: int
    title: str
    voteAverage: Optional[float]
    voteCount: Optional[float]
    status: Optional[str]
    releaseDate: Optional[str]
    revenue: Optional[float]
    runtime: Optional[float]
    budget: Optional[float]
    imdbId: Optional[str]
    originalLanguage: Optional[str] 
    originalTitle: Optional[str]
    overview: Optional[str]
    popularity: Optional[float]
    genres: Optional[list[str]]
    productionCompanies: Optional[list[str]]
    productionCountries: Optional[list[str]]
    spokenLanguages: Optional[list[str]]
    cast: Optional[list[str]]
    director: Optional[list[str]]
    directorOfPhotography: Optional[list[str]]
    writers: Optional[list[str]]
    producers: Optional[list[str]]
    musicComposer: Optional[list[str]]
    imdbRating: Optional[float]
    imdbVotes: Optional[float]
    posterPath: Optional[str]
    backdropPath: Optional[str]

class RequestDict(BaseModel):
    fromUsername: Optional[str]
    requestId: str
    recentlyLikedMovies: List[MovieDto]


class Request:
    requestId: str
    serviceId: str
    payload: MovieDto

class MessageRequest(BaseModel):
    requestId: str
    serviceId: str
    payload: Dict


class RecommendationResponse(BaseModel):
    requestId: str
    recommendationsId: List[int]

# ActiveMQ connection settings
activemq_host = "activemq"
activemq_port = 61613  # STOMP port
activemq_queue = "/queue/recommendation-movie.queue"
activemq_queue_response = "/queue/recommendation-movie-response.queue"
activemq_user = "admin"
activemq_password = "cine"

# Storage for received messages
received_messages = []

class MessageListener(stomp.ConnectionListener):
    def __init__(self, activemq_client):
        self.activemq_client = activemq_client

    def on_error(self, frame):
        print(f'Error: {frame.body}')

    def on_message(self, frame):
        # message = json.loads(frame.body)
        # print(f'[Orchestrator → FastAPI]: {message}')
        # received_messages.append(message)
        # activemq_client.send_message(f"This is a answer to the message received : {message}", destination_queue=activemq_queue_response)
        try:
            # pure vibe coding
            # This is your raw string from frame.body
            parsed_message = MessageRequest.parse_raw(frame.body)
            # print(parsed_message)
            movie_json_string = parsed_message.payload.get("payload")
            if movie_json_string:
                try:
                    recentlyLikedMovies = movie_json_string.get("recentlyLikedMovies") 
                    # print("first movie liked: ", recentlyLikedMovies[0].get("title"))
                except json.JSONDecodeError as e:
                    print(f"❌ Error decoding movie JSON: {e}")
            else:
                print("❗No 'payload' key in payload")

            # Traitement AI
            movie_ids = []
            for movie in recentlyLikedMovies:
                movie_ids.append(movie.get("id"))

            # movie_ids = [
            #     "157336",  # Interstellar
            # ]
            # Contains ID
            recommended_movies = recommend_movies(df_full, model, movie_ids, topn=10)

            #
            response = {
                "requestId": movie_json_string.get("requestId"),  # UUID as string
                "recommendationsId": recommended_movies  # List of Strings (just Python ints)
            }
            activemq_client.send_message(response, destination_queue=activemq_queue_response)
        except Exception as e:
            print(f"[FastAPI] Erreur de parsing du message : {e}")

       
class ActiveMQClient:
    def __init__(self, host, port, username, password, queue):
        self.host = host
        self.port = port
        self.username = username
        self.password = password
        self.queue = queue
        
        # Producer connection
        self.producer_conn = stomp.Connection([(host, port)])
        self.connect_producer()
        
        # Consumer connection (separate from producer to avoid conflicts)
        self.consumer_conn = stomp.Connection([(host, port)])
        self.listener = MessageListener(self)
        self.consumer_conn.set_listener('', self.listener)
        self.connect_consumer()
    
    def connect_producer(self):
        if not self.producer_conn.is_connected():
            try:
                self.producer_conn.connect('admin', 'cine', wait=True)
                print("producer connected {", self.producer_conn.is_connected(), "}")
            except Exception as e:
                print(e)

    def connect_consumer(self):
        if not self.consumer_conn.is_connected():
            try:
                self.consumer_conn.connect(self.username, self.password, wait=True)
                self.consumer_conn.subscribe(destination=self.queue, id=1, ack='auto')
                print(f"[FastAPI] Subscribed to {self.queue}")
            except Exception as e:
                print(e)

    def send_message(self, message, destination_queue=None):
        try:
            if not self.producer_conn.is_connected():
                self.connect_producer()
            if destination_queue is None:
                destination_queue = self.queue
            print(json.dumps(message))
            self.producer_conn.send(
                destination=destination_queue,
                body=json.dumps(message),
                headers = {
                    "content-type": "application/json"
                }
            )
            return True
        except Exception as e:
            print(f"Error sending message: {e}")
            return False
    
    def disconnect(self):
        if self.producer_conn.is_connected():
            self.producer_conn.disconnect()
        if self.consumer_conn.is_connected():
            self.consumer_conn.disconnect()

# Initialize the ActiveMQ client
activemq_client = ActiveMQClient(
    activemq_host, 
    activemq_port,
    activemq_user,
    activemq_password,
    activemq_queue
)

@app.post("/send-message/")
async def send_message(message: dict):
    """Send a message to ActiveMQ queue"""
    if activemq_client.send_message(message, activemq_queue_response):
        return {"status": "Message sent successfully", "message": message}
    else:
        return {"status": "Error sending message"}, 500

@app.get("/messages/")
async def get_messages():
    """Get all received messages from ActiveMQ queue"""
    return {"messages": received_messages}

@app.post("/clear-messages/")
async def clear_messages():
    """Clear the stored messages"""
    received_messages.clear()
    return {"status": "Messages cleared"}

@app.get("/health/")
async def health_check():
    """Check if connections to ActiveMQ are alive"""
    producer_status = activemq_client.producer_conn.is_connected()
    consumer_status = activemq_client.consumer_conn.is_connected()
    
    # Try to reconnect if connections are down
    if not producer_status:
        activemq_client.connect_producer()
        producer_status = activemq_client.producer_conn.is_connected()
    
    if not consumer_status:
        activemq_client.connect_consumer()
        consumer_status = activemq_client.consumer_conn.is_connected()
    
    return {
        "status": "healthy" if producer_status and consumer_status else "unhealthy",
        "producer_connected": producer_status,
        "consumer_connected": consumer_status
    }

@app.on_event("shutdown")
def shutdown_event():
    """Clean up connections when shutting down"""
    activemq_client.disconnect()