package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import fr.cpe.cinematch_backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
public class ProfilController {

    @Autowired
    private ProfilService profilService;
    @Autowired
    private ProfilRepository profilRepository;
    @Autowired
    private LovedMovieService lovedMovieService;
    @Autowired
    private WatchedMovieService watchedMovieService;
    @Autowired
    private WatchlistMovieService watchlistMovieService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserConfigurationService userConfigurationService;

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails)
            throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getProfileByUsername(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProfileDto profileDto)
            throws GenericNotFoundException {
        // on ne transmet que les champs description + isChild
        profilService.updateProfile(userDetails.getUsername(), profileDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/picture/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.getSize() > 150 * 1024) {
                return ResponseEntity.badRequest().body("Image trop lourde (max 100 Ko).");
            }

            profilService.saveProfilePicture(userDetails.getUsername(), file);
            return ResponseEntity.ok().build();

        } catch (IOException | GenericNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de l’upload : " + e.getMessage());
        }
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> deletePicture(@AuthenticationPrincipal UserDetails userDetails)
            throws GenericNotFoundException {
        profilService.deleteProfilePicture(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getProfileByUsername(@PathVariable String username)
            throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getProfileByUsername(username));
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUserByUserId() throws GenericNotFoundException, BadEndpointException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser uE = (AppUser) authentication.getPrincipal();

        lovedMovieService.deleteAllByUserId(uE.getId());
        watchedMovieService.deleteAllByUserId(uE.getId());
        watchlistMovieService.deleteAllByUserId(uE.getId());
        friendshipService.deleteAllByUserId(uE.getId());
        friendRequestService.deleteAllByUserId(uE.getId());
        reviewService.deleteAllByUserId(uE.getId());
        conversationService.deleteAllConversationsAndMessagesByUserId(uE.getId());
        userConfigurationService.deleteUserConfiguration(uE.getId());

        profilService.deleteProfil(uE);
        appUserService.deleteUserById(uE);

        return ResponseEntity.noContent().build();
    }
}
