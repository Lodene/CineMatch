package fr.cpe.cinematch_backend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.FriendUser;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.FriendUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.dtos.FriendDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendUserRepository friendUserRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProfilRepository profilRepository;

    public void createFriendship(Long id1, Long id2) throws BadEndpointException {
        if (id1.equals(id2)) {
            throw new BadEndpointException(400, "Invalid friendship", "A user cannot befriend themselves");
        }

        Long userId1 = Math.min(id1, id2);
        Long userId2 = Math.max(id1, id2);

        if (friendUserRepository.findByUserId1AndUserId2(userId1, userId2).isPresent()) {
            throw new BadEndpointException(409, "Friendship already exists", "These users are already friends");
        }

        FriendUser friend = new FriendUser();
        friend.setUserId1(userId1);
        friend.setUserId2(userId2);
        friend.setCreatedAt(new Date());
        friendUserRepository.save(friend);
    }

    @Transactional
    public void deleteFriendship(String friendUsername, String currentUsername) throws GenericNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable", "Utilisateur courant introuvable"));

        AppUser friendUser = appUserRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable", "Aucun utilisateur avec ce nom d'utilisateur"));

        Long userId1 = Math.min(currentUser.getId(), friendUser.getId());
        Long userId2 = Math.max(currentUser.getId(), friendUser.getId());

        FriendUser friendship = friendUserRepository.findByUserId1AndUserId2(userId1, userId2)
                .orElseThrow(() -> new GenericNotFoundException(404, "Amitié non trouvée", "Aucune amitié existante entre ces utilisateurs"));

        friendUserRepository.delete(friendship);
    }

    public List<FriendDTO> getFriendsOfUser(String currentUsername) throws GenericNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable", "Utilisateur courant introuvable"));

        List<FriendUser> friendships = friendUserRepository.findByUserId1OrUserId2(currentUser.getId(), currentUser.getId());

        List<FriendDTO> friends = new ArrayList<>();
        for (FriendUser friendship : friendships) {
            Long friendId = friendship.getUserId1().equals(currentUser.getId())
                    ? friendship.getUserId2()
                    : friendship.getUserId1();

            AppUser friend = appUserRepository.findById(friendId)
                    .orElseThrow(() -> new GenericNotFoundException(404, "Ami introuvable", "Impossible de retrouver l'utilisateur ami"));

            ProfileEntity profil = profilRepository.findByUserId(friend.getId())
                    .orElseThrow(() -> new GenericNotFoundException(404, "Profil manquant", "Profil associé à l'utilisateur ami introuvable"));

            friends.add(new FriendDTO(friend.getUsername(), profil.getProfilPicture()));
        }

        return friends;
    }
}
