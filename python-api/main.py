from fastapi import FastAPI
import stomp
import json
import uuid
from pydantic import BaseModel
from typing import Dict

app = FastAPI(title="FastAPI ActiveMQ Integration")




class MessageRequest(BaseModel):
    requestId: str
    serviceId: str
    payload: Dict


# ActiveMQ connection settings
activemq_host = "activemq"
activemq_port = 61613  # STOMP port
activemq_queue = "/queue/service-movie.queue"
activemq_queue_response = "/queue/model-in.queue"
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
            parsed_message = MessageRequest.parse_raw(frame.body)
            print("partie MSG-----------------")
            print(f"[Orchestrator → FastAPI] Message reçu et validé : {parsed_message}")

            # Enregistre la version dict pour l’API
            received_messages.append(parsed_message.dict())

            # Envoie à model-in.queue
            self.activemq_client.send_message(parsed_message.dict(), destination_queue=activemq_queue_response)
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
                self.producer_conn.connect(self.username, self.password, wait=True)
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

            self.producer_conn.send(
                destination=destination_queue,
                body=json.dumps(message),
                content_type='application/json',
                headers={'message-id': str(uuid.uuid4())}
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
    if activemq_client.send_message(message):
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