package cpe.cinematch_backend.orchestrator.controllers;

import cpe.cinematch_backend.orchestrator.requests.MovieRecommendationRequest;
import cpe.cinematch_backend.orchestrator.services.SimilarMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController
{

	@Autowired
	private SimilarMovieService similarMovieService;



	@PostMapping("/recommendation-film")
	public String getRecommendationFilm(@RequestBody MovieRecommendationRequest movieRecommendationRequest) {
		UUID uuid = UUID.randomUUID();
		similarMovieService.sendRequest(uuid, movieRecommendationRequest);
		return uuid.toString();
	}
}
