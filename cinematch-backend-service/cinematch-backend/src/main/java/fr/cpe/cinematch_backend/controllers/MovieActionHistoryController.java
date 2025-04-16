package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.services.MovieActionHistoryService;
import fr.cpe.cinematch_backend.dtos.PaginatedHistoryResponseDto;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;

import fr.cpe.cinematch_backend.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/history")
public class MovieActionHistoryController {

        @Autowired
        private AppUserRepository appUserRepository;
        @Autowired
        private MovieActionHistoryService historyService;

        @GetMapping("/history")
        public ResponseEntity<PaginatedHistoryResponseDto> getUserMovieHistory(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(userDetails.getUsername())
                                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur non trouvé",
                                                "Utilisateur introuvable"));
                return ResponseEntity.ok(historyService.getUserHistoryPaginated(user.getId(), page, size));
        }

        @GetMapping("/history/{username}")
        public ResponseEntity<PaginatedHistoryResponseDto> getUserMovieHistoryByUsername(
                        @PathVariable String username,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(username)
                                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur non trouvé",
                                                "Aucun utilisateur avec ce nom d'utilisateur"));
                return ResponseEntity.ok(historyService.getUserHistoryPaginated(user.getId(), page, size));
        }
}
