package cpe.cinematch_backend.orchestrator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_asi_ii.MessageRequest;
import cpe.cinematch_backend.orchestrator.entities.MovieRecommendationEntity;
import cpe.cinematch_backend.orchestrator.repositories.MovieRecommendationRepository;
import cpe.cinematch_backend.orchestrator.requests.MovieRecommendationActiveMqRequest;
import cpe.cinematch_backend.orchestrator.requests.MovieRecommendationRequest;
import cpe.cinematch_backend.orchestrator.requests.SocketRecommendationRequest;
import cpe.cinematch_backend.orchestrator.responses.MovieRecommendationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationMovieService {

    @Autowired
    MovieRecommendationRepository movieRecommendationRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Environment env;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendRequest(UUID uuid, MovieRecommendationRequest movieRecommendationRequest) {

        String serviceId = env.getProperty("activemq.recommendation-movie.queue");
        String serviceName = env.getProperty("activemq.recommendation-movie.service");
        // fixme: custom error handling
        if (serviceName == null || serviceId == null) {
            throw new RuntimeException("Error: serviceName or serviceId is null");
        }
        // enregistrement de la transaction pour rollback & retour
        MovieRecommendationEntity movieRecommendationEntity = new MovieRecommendationEntity();
        movieRecommendationEntity.setId(uuid);
        // todo: maybe save the whole movie?
        movieRecommendationEntity.setFromUsername(movieRecommendationRequest.getFromUsername());
        movieRecommendationEntity.setRecentlyLikedMovies(movieRecommendationRequest.getRecentlyLikedMovies());
        movieRecommendationEntity.setRecommendationsId(new ArrayList<>());
        movieRecommendationRepository.save(movieRecommendationEntity);

        MovieRecommendationActiveMqRequest movieRequest = new MovieRecommendationActiveMqRequest();
        movieRequest.setRequestId(uuid.toString());
        movieRequest.setRecentlyLikedMovies(movieRecommendationRequest.getRecentlyLikedMovies());
        movieRequest.setFromUsername(movieRecommendationRequest.getFromUsername());

        try {

            // ObjectMapper mapper = new ObjectMapper();
            // String jsonObject = objectMapper.writeValueAsString(movieRequest);

            MessageRequest message = new MessageRequest(uuid.toString(), serviceId, Map.of(
                    "payload" , movieRequest));
            String jsonMessage = objectMapper.writeValueAsString(message); // on convertit l'objet Java en JSON
            jmsTemplate.convertAndSend(serviceId, jsonMessage); // on envoie du JSON texte, pas un objet
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @JmsListener(destination = "recommendation-movie-response.queue", containerFactory = "myFactory")
    public void receiveRequest(byte[] movieRecommendationResponseJson){

        String serviceId = env.getProperty("activemq.recommendation-movie-response.queue");
        String serviceName = env.getProperty("activemq.recommendation-movie.service");
        if (serviceName == null || serviceId == null) {
            throw new RuntimeException("Error: serviceName or serviceId is null");
        }
        MovieRecommendationResponse response;
        try {
            String json = new String(movieRecommendationResponseJson, StandardCharsets.UTF_8);  // decode byte[] properly
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(json, MovieRecommendationResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<MovieRecommendationEntity> movieRecommendationEntity = movieRecommendationRepository.findById(response.getRequestId());
        if (movieRecommendationEntity.isPresent()) {
            // save in db for rollback purpose
            movieRecommendationEntity.get().setRecommendationsId(response.getRecommendationsId());
            movieRecommendationRepository.save(movieRecommendationEntity.get());
            // notify the monolith to fetch movie info
            this.postRequest(response, movieRecommendationEntity.get(), 0);
        }

    }

    private void postRequest(MovieRecommendationResponse response, MovieRecommendationEntity movieRecommendationEntity, int tryCount) {
        String monolithUrl = env.getProperty("monolith.url");
        if (monolithUrl == null) {
            // bad config
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SocketRecommendationRequest socketRequest = new SocketRecommendationRequest();
        socketRequest.setRequestId(response.getRequestId());
        socketRequest.setFromUsername(movieRecommendationEntity.getFromUsername());
        socketRequest.setRecommendationsId(response.getRecommendationsId());
        HttpEntity<SocketRecommendationRequest> requestEntity = new HttpEntity<>(socketRequest, headers);
        String result = restTemplate.postForObject(monolithUrl.concat("movie/getRecommendedFilm"), requestEntity, String.class);
        if (result != null) {
            // everything went well, we can delete
            movieRecommendationRepository.delete(movieRecommendationEntity);
        } else {
            if (tryCount >= 3) {
                // 3 try max
                return;
            }
            // retry until success
            tryCount +=1;
            this.postRequest(response, movieRecommendationEntity, tryCount);
        }

    }
}
