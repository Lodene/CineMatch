package cpe.cinematch_backend.orchestrator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_asi_ii.MessageRequest;
import cpe.cinematch_backend.orchestrator.entities.SimilarMovieRequestEntity;
import cpe.cinematch_backend.orchestrator.repositories.SimilarMovieRequestRepository;
import cpe.cinematch_backend.orchestrator.requests.SimilarMovieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Service
public class SimilarMovieService {

    @Autowired
    SimilarMovieRequestRepository similarMovieRequestRepository;


    @Autowired
    private Environment env;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendRequest(UUID uuid, SimilarMovieRequest similarMovieRequest) {

        String serviceId = env.getProperty("activemq.similar-movie.queue");
        String serviceName = env.getProperty("activemq.similar-movie.service");
        // fixme: custom error handling
        if (serviceName == null || serviceId == null) {
            throw new RuntimeException("Error: serviceName or serviceId is null");
        }
        // enregistrement de la transaction pour rollback & retour
        SimilarMovieRequestEntity similarMovieRequestEntity = new SimilarMovieRequestEntity();
        similarMovieRequestEntity.setId(uuid);
        // todo: maybe save the whole movie?
        similarMovieRequestEntity.setOriginalMovieId(similarMovieRequest.getMovie().getId());
        similarMovieRequestEntity.setSimilarMoviesId(new ArrayList<>());
        similarMovieRequestRepository.save(similarMovieRequestEntity);


        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = objectMapper.writeValueAsString(similarMovieRequest);
            MessageRequest message = new MessageRequest(uuid.toString(), serviceId, Map.of(
                    "movie" , jsonObject));
            String jsonMessage = objectMapper.writeValueAsString(message); // on convertit l'objet Java en JSON
            jmsTemplate.convertAndSend(serviceId, jsonMessage); // on envoie du JSON texte, pas un objet
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
