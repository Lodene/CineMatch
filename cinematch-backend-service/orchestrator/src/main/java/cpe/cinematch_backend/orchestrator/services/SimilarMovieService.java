package cpe.cinematch_backend.orchestrator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_asi_ii.MessageRequest;
import cpe.cinematch_backend.orchestrator.entities.MovieRecommendationEntity;
import cpe.cinematch_backend.orchestrator.repositories.MovieRecommendationRepository;
import cpe.cinematch_backend.orchestrator.requests.MovieRecommendationRequest;
import cpe.cinematch_backend.orchestrator.responses.MovieRecommendationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimilarMovieService {

    @Autowired
    MovieRecommendationRepository movieRecommendationRepository;


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


        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = objectMapper.writeValueAsString(movieRecommendationRequest);
            MessageRequest message = new MessageRequest(uuid.toString(), serviceId, Map.of(
                    "movie" , jsonObject));
            String jsonMessage = objectMapper.writeValueAsString(message); // on convertit l'objet Java en JSON
            jmsTemplate.convertAndSend(serviceId, jsonMessage); // on envoie du JSON texte, pas un objet
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @JmsListener(destination = "recommendation-movie-response.queue")
    public void receiveRequest(MovieRecommendationResponse movieRecommendationResponse){
        String serviceId = env.getProperty("activemq.recommendation-movie-response.queue");
        String serviceName = env.getProperty("activemq.recommendation-movie.service");
        if (serviceName == null || serviceId == null) {
            throw new RuntimeException("Error: serviceName or serviceId is null");
        }
        Optional<MovieRecommendationEntity> similarMovieRequest = movieRecommendationRepository.findById(movieRecommendationResponse.getRequestId());
        if (similarMovieRequest.isPresent()) {
            // save in db for rollback purpose
            similarMovieRequest.get().setRecommendationsId(movieRecommendationResponse.getRecommendationsId());
            movieRecommendationRepository.save(similarMovieRequest.get());

            // todo: make an api call to the node js to notify the frontend
        }
    }

}
