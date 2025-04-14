package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.FriendRequestDto;
import fr.cpe.cinematch_backend.dtos.FriendRequestResponseDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.FriendRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping
    public void sendFriendRequest(@RequestBody @Valid FriendRequestDto dto) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        friendRequestService.sendFriendRequest(user.getUsername(), dto);
    }

    @PostMapping("/respond")
    public void respondToFriendRequest(@RequestBody @Valid FriendRequestResponseDto dto)
            throws GenericNotFoundException {
        friendRequestService.respondToFriendRequest(dto);
    }
}
