package cpe.cinematch_backend.orchestrator.services;

import com.projet_asi_ii.MessageRequest;
import cpe.cinematch_backend.orchestrator.entities.MovieEntity;
import cpe.cinematch_backend.orchestrator.repositories.MovieRepository;
import cpe.cinematch_backend.orchestrator.requests.PromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MovieService
{
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MovieRepository movieRepository;

	private String bearerToken;


	private static final String SERVICE_BACK_API_URL = "http://spring-back:8081/movie";

//	private static final String SERVICE_2_API_URL = "http://logger:8089/messages";

	public void sendRequests(UUID requestId, String userToken, PromptRequest promptRequest) {
		this.bearerToken = userToken;
		movieRepository.save(new MovieEntity(requestId));

		// map des services connecté à la queue
		Map<String, String> serviceQueues = Map.of(
				"movie-recommandation", "service-movie.queue"
		);

		for (Map.Entry<String, String> entry : serviceQueues.entrySet()) {
			String serviceId = entry.getKey();
			String queueName = entry.getValue();

			MessageRequest message = new MessageRequest(requestId.toString(), serviceId, Map.of("name", promptRequest.getName(), "prompt", promptRequest.getPrompt()));
			jmsTemplate.convertAndSend(queueName, message);
		}
	}

	@JmsListener(destination = "response.queue")
	public void receiveResponse(MessageRequest message) {
		RestTemplate restTemplate = new RestTemplate();
		// restTemplate.postForEntity(SERVICE_2_API_URL, message, String.class);

		Optional<MovieEntity> optionalMovieEntity = movieRepository.findByMovieId(UUID.fromString(message.getRequestId()));
		if (optionalMovieEntity.isPresent())
		{
			MovieEntity finalCard = optionalMovieEntity.get();
			/*	switch en fonction de la réponse
			switch (message.getServiceId()) {
				case "card-image":
					finalCard.setImage((String) message.getPayload().get("data"));
					movieRepository.save(finalCard);
					break;
				case "card-prompt":
					finalCard.setPrompt((String) message.getPayload().get("data"));
					movieRepository.save(finalCard);
					break;
				default:
					break;
			}
			*/
			/* Todo: refacto la barrière de synchro
			if (finalCard.getPrompt() == null || finalCard.getImage() == null)
			{
				return;
			}
			*/
			// on réinsère le token pour passer l'authent
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(bearerToken.replace("Bearer ", ""));

			HttpEntity<MovieEntity> requestEntity = new HttpEntity<>(finalCard, headers);

			restTemplate.postForObject(SERVICE_BACK_API_URL, requestEntity, String.class);
		}
	}
}
