package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.FriendDTO;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.AppUserService;
import fr.cpe.cinematch_backend.services.FriendService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<FriendDTO>> getMyFriends(@AuthenticationPrincipal UserDetails userDetails) throws GenericNotFoundException {
        String currentUsername = userDetails.getUsername();
        List<FriendDTO> friends = friendService.getFriendsOfUser(currentUsername);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteFriendship(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String username) throws GenericNotFoundException {
        String currentUsername = userDetails.getUsername();
        friendService.deleteFriendship(currentUsername, username);
        return ResponseEntity.noContent().build();
    }
}
