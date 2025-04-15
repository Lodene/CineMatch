package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.FriendRequestResponseDto;
import fr.cpe.cinematch_backend.dtos.IncomingFriendRequestDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.FriendRequestEntity;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.FriendRequestRepository;
import fr.cpe.cinematch_backend.repositories.FriendShipRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private FriendShipRepository friendShipRepository;

    public void sendFriendRequest(String senderUsername, String username)
            throws GenericNotFoundException, BadEndpointException {
        AppUser sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Sender not found"));
        AppUser receiver = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Receiver not found"));

        if (friendShipRepository.findByUserId1AndUserId2(sender.getId(), receiver.getId()).isPresent()) {
            throw new BadEndpointException(400, "FriendshipEntity already exists", "These users are already friends");
        }

        if (friendShipRepository.findByUserId1AndUserId2(receiver.getId(), sender.getId()).isPresent()) {
            throw new BadEndpointException(400, "FriendshipEntity already exists", "These users are already friends");
        }
        if (this.checkIfFriendRequestAlreadyExist(sender.getId(), receiver.getId())) {
            throw new BadEndpointException(400, "Friend request already sent",
                    "A friend request is already pending");
        }

        FriendRequestEntity request = new FriendRequestEntity();
        request.setAskedById(sender.getId());
        request.setToId(receiver.getId());
        friendRequestRepository.save(request);
    }

    // return true if demand was accepted, false otherwise
    public void acceptFriendRequest(Long requestId, Long currentUserId) throws GenericNotFoundException {
        FriendRequestEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Demande introuvable", "Aucune demande avec cet identifiant"));

        if (!request.getToId().equals(currentUserId)) {
            throw new GenericNotFoundException(403, "Action non autorisée", "Vous ne pouvez pas accepter cette demande");
        }

        try {
            friendshipService.createFriendship(currentUserId, request.getAskedById());
        } catch (BadEndpointException e) {
            throw new GenericNotFoundException(409, "Erreur lors de la création de l'amitié", e.getMessage());
        }

        friendRequestRepository.deleteById(requestId);
    }

    public void declineFriendRequest(Long requestId, Long currentUserId) throws GenericNotFoundException {
        FriendRequestEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Demande introuvable", "Aucune demande avec cet identifiant"));

        if (!request.getToId().equals(currentUserId)) {
            throw new GenericNotFoundException(403, "Action non autorisée", "Vous ne pouvez pas refuser cette demande");
        }

        friendRequestRepository.deleteById(requestId);
    }

    public List<IncomingFriendRequestDto> getReceivedFriendRequests(String username) throws GenericNotFoundException {
        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "The current user was not found"));

        List<FriendRequestEntity> receivedRequests = friendRequestRepository.findAll().stream()
                .filter(r -> r.getToId().equals(currentUser.getId()))
                .toList();

        List<IncomingFriendRequestDto> result = new ArrayList<>();

        for (FriendRequestEntity request : receivedRequests) {
            AppUser sender = userRepository.findById(request.getAskedById())
                    .orElseThrow(
                            () -> new GenericNotFoundException(404, "Sender not found", "The sender's user was not found"));

            ProfileEntity profil = profilRepository.findByUserId(sender.getId())
                    .orElseThrow(() -> new GenericNotFoundException(404, "Profile is missing",
                            "The sender's profile was not found"));

            result.add(new IncomingFriendRequestDto(
                    request.getId(),
                    sender.getUsername(),
                    profil.getProfilPicture()));
        }

        return result;
    }

    private boolean checkIfFriendRequestAlreadyExist(Long senderId, Long userId) {
        Optional<FriendRequestEntity> friendRequestEntity = friendRequestRepository.findByToIdAndAskedById(senderId, userId);
        if (friendRequestEntity.isEmpty()) {
            // on vérifie dans l'autre sens
            friendRequestEntity = friendRequestRepository.findByToIdAndAskedById(userId, senderId);
            return friendRequestEntity.isPresent();
        } else {
            return true;
        }
    }

    @Transactional
    public void deleteAllFriendRequestsByUserId(Long userId) {
        List<FriendRequestEntity> allRequests = friendRequestRepository.findAll();
        List<FriendRequestEntity> toDelete = allRequests.stream()
                .filter(r -> r.getAskedById().equals(userId) || r.getToId().equals(userId))
                .toList();

        friendRequestRepository.deleteAll(toDelete);
    }

}
