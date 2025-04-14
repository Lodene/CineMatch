package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.FriendRequestDto;
import fr.cpe.cinematch_backend.dtos.FriendRequestResponseDto;
import fr.cpe.cinematch_backend.dtos.IncomingFriendRequestDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.FriendRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/{username}")
    public ResponseEntity sendFriendRequest(@PathVariable String username)
            throws GenericNotFoundException, BadEndpointException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        friendRequestService.sendFriendRequest(user.getUsername(), username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/respond")
    public ResponseEntity respondToFriendRequest(@RequestBody @Valid FriendRequestResponseDto dto)
            throws GenericNotFoundException {
        friendRequestService.respondToFriendRequest(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<IncomingFriendRequestDto> getReceivedFriendRequests() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return friendRequestService.getReceivedFriendRequests(user.getUsername());
    }
}
