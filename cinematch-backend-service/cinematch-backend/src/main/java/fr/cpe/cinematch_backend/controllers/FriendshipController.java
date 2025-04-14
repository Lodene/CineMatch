package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.FriendDTO;
import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;


    @GetMapping
    public ResponseEntity<List<ProfileDto>> getMyFriends(@AuthenticationPrincipal UserDetails userDetails) throws GenericNotFoundException {
        String currentUsername = userDetails.getUsername();
        return ResponseEntity.ok(friendshipService.getFriendsOfUser(currentUsername));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteFriendship(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String username) throws GenericNotFoundException {
        String currentUsername = userDetails.getUsername();
        friendshipService.deleteFriendship(currentUsername, username);
        return ResponseEntity.noContent().build();
    }
}
