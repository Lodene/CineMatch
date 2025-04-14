package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.ProfilService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getProfileByUsername(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody ProfileDto profileDto) throws GenericNotFoundException {
        profilService.updateProfile(userDetails.getUsername(), profileDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/picture")
    public ResponseEntity<Void> updatePicture(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody String path) throws GenericNotFoundException {
        profilService.updateProfilePicture(userDetails.getUsername(), path);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> deletePicture(@AuthenticationPrincipal UserDetails userDetails) throws GenericNotFoundException {
        profilService.deleteProfilePicture(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getProfileByUsername(@PathVariable String username) throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getProfileByUsername(username));
    }
}
