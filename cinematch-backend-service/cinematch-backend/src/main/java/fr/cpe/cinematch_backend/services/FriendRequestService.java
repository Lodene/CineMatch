package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.FriendDTO;
import fr.cpe.cinematch_backend.dtos.FriendRequestDto;
import fr.cpe.cinematch_backend.services.FriendService;
import fr.cpe.cinematch_backend.dtos.FriendRequestResponseDto;
import fr.cpe.cinematch_backend.dtos.IncomingFriendRequestDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.FriendRequestEntity;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.FriendRequestRepository;
import fr.cpe.cinematch_backend.repositories.FriendUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private FriendUserRepository friendUserRepository;

    public void sendFriendRequest(String senderUsername, FriendRequestDto dto)
            throws GenericNotFoundException, BadEndpointException {
        AppUser sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Sender not found"));
        AppUser receiver = userRepository.findById(dto.getTo())
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Receiver not found"));

        if (friendUserRepository.findByUserId1AndUserId2(sender.getId(), receiver.getId()).isPresent()) {
            throw new BadEndpointException(409, "Friendship already exists", "These users are already friends");
        }

        if (friendUserRepository.findByUserId1AndUserId2(receiver.getId(), sender.getId()).isPresent()) {
            throw new BadEndpointException(409, "Friendship already exists", "These users are already friends");
        }

        // 🔁 (Optionnel) Vérifier si une demande est déjà en cours
        boolean alreadyRequested = friendRequestRepository.findAll().stream()
                .anyMatch(r -> r.getAskedBy().equals(sender.getId()) && r.getTo().equals(receiver.getId()));
        if (alreadyRequested) {
            throw new GenericNotFoundException(409, "Friend request already sent",
                    "A friend request is already pending");
        }

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

        if (dto.isAccepted()) {
            try {
                // Création du lien d'amitié si accepté
                friendService.createFriendship(request.getAskedBy(), request.getTo());
            } catch (BadEndpointException e) {
                throw new GenericNotFoundException(409, "Friendship error", e.getMessage());
            }
        }

        // Suppression de la demande (acceptée ou refusée)
        friendRequestRepository.deleteById(request.getId());
    }

    public List<IncomingFriendRequestDto> getReceivedFriendRequests(String username) throws GenericNotFoundException {
        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable",
                        "Utilisateur courant introuvable"));

        List<FriendRequestEntity> receivedRequests = friendRequestRepository.findAll().stream()
                .filter(r -> r.getTo().equals(currentUser.getId()))
                .toList();

        List<IncomingFriendRequestDto> result = new ArrayList<>();

        for (FriendRequestEntity request : receivedRequests) {
            AppUser sender = userRepository.findById(request.getAskedBy())
                    .orElseThrow(
                            () -> new GenericNotFoundException(404, "Expéditeur introuvable", "Expéditeur non trouvé"));

            ProfileEntity profil = profilRepository.findByUserId(sender.getId())
                    .orElseThrow(() -> new GenericNotFoundException(404, "Profil manquant",
                            "Profil de l'expéditeur introuvable"));

            result.add(new IncomingFriendRequestDto(
                    request.getId(),
                    sender.getUsername(),
                    profil.getProfilPicture()));
        }

        return result;
    }

}
