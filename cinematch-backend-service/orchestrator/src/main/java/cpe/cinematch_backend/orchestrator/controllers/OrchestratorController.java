package cpe.cinematch_backend.orchestrator.controllers;

import cpe.cinematch_backend.orchestrator.requests.PromptRequest;
import cpe.cinematch_backend.orchestrator.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController
{

	@Autowired
	private MovieService movieService;

	@PostMapping("/generateCard")
	public String generateCard(@RequestHeader("Authorization") String bearerToken, @RequestBody PromptRequest promptRequest) {
		movieService.sendRequests(UUID.randomUUID(), bearerToken, promptRequest);
		return "Request sent";
	}
}
