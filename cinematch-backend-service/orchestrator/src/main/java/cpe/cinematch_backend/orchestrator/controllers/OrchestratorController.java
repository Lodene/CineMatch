package cpe.cinematch_backend.orchestrator.controllers;

import cpe.cinematch_backend.orchestrator.requests.MovieRecommendationRequest;
import cpe.cinematch_backend.orchestrator.services.RecommendationMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController
{

	@Autowired
	private RecommendationMovieService recommendationMovieService;



	@PostMapping("/recommendation-film")
	public String getRecommendationFilm(@RequestBody MovieRecommendationRequest movieRecommendationRequest) {
		UUID uuid = UUID.randomUUID();
		recommendationMovieService.sendRequest(uuid, movieRecommendationRequest);
		return uuid.toString();
	}
}
