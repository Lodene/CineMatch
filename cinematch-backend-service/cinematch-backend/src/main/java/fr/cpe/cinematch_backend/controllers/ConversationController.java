package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.ConversationDto;
import fr.cpe.cinematch_backend.dtos.requests.MessageRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getUserConversations() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUser uE = (AppUser) authentication.getPrincipal();
            return ResponseEntity.ok(conversationService.getUserConversations(uE.getUsername()));
        } catch (Exception e) {
            e.printStackTrace(); // ou log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequest request) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser sender = (AppUser) authentication.getPrincipal();

        conversationService.sendMessage(sender.getId(), request);
        return ResponseEntity.ok().build();
    }
}
