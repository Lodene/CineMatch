package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.FriendRequestDto;
import fr.cpe.cinematch_backend.dtos.FriendRequestResponseDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.FriendRequestEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AppUserRepository userRepository;

    public void sendFriendRequest(String senderUsername, FriendRequestDto dto) throws GenericNotFoundException {
        AppUser sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Sender not found"));
        AppUser receiver = userRepository.findById(dto.getTo())
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Receiver not found"));

        FriendRequestEntity request = new FriendRequestEntity();
        request.setAskedBy(sender.getId());
        request.setTo(receiver.getId());
        request.setCreatedAt(LocalDateTime.now());
        request.setModifiedAt(LocalDateTime.now());
        friendRequestRepository.save(request);
    }

    public void respondToFriendRequest(FriendRequestResponseDto dto) throws GenericNotFoundException {
        FriendRequestEntity request = friendRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new GenericNotFoundException(404, "Request not found", "Request ID not found"));

        // TODO: gérer la logique de création de lien d'amitié si accepté
        // if (dto.isAccepted()) { ... }

        friendRequestRepository.deleteById(request.getId());
    }
}
