package cpe.cinematch_backend.orchestrator.controllers;

import cpe.cinematch_backend.orchestrator.requests.SimilarMovieRequest;
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



	@PostMapping("/similar-film")
	public String getSimiliarFilm(@RequestBody SimilarMovieRequest similarMovieRequest) {
		UUID uuid = UUID.randomUUID();
		similarMovieService.sendRequest(uuid, similarMovieRequest);
		return uuid.toString() ;
	}
}
