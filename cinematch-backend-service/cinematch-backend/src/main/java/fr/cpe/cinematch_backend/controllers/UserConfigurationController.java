package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.UserConfigurationService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/preferences")
public class UserConfigurationController {

    @Autowired
    private UserConfigurationService userPreferencesService;

    @PutMapping("/lang")
    public ResponseEntity<Void> updateLangPreference(@RequestParam String lang)
            throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        userPreferencesService.updateLanguagePreference(user.getUsername(), lang);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lang")
    public ResponseEntity<Optional<String>> getLangPreference() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return ResponseEntity.ok(userPreferencesService.getUserLanguage(user.getUsername()));
    }
}
