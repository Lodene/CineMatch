package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ProfileDetailsDto;
import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.FriendshipEntity;
import fr.cpe.cinematch_backend.mappers.ProfileMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.FriendShipRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    @Autowired
    private FriendShipRepository friendShipRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilService profilService;



    public void createFriendship(Long userId1, Long userId2) throws BadEndpointException {
        if (userId1.equals(userId2)) {
            throw new BadEndpointException(400, "Invalid friendshipEntity", "A user cannot befriend themselves");
        }
        if (this.checkIfFriendShipAlreadyExist(userId1, userId2)) {
            throw new BadEndpointException(400, "Invalid friendshipEntity", "The users are already friends");
        }

        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setUserId1(userId1);
        friendshipEntity.setUserId2(userId2);
        friendshipEntity.setCreatedAt(new Date());
        friendShipRepository.save(friendshipEntity);
    }

    @Transactional
    public void deleteFriendship(String friendUsername, String currentUsername) throws GenericNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable",
                        "Utilisateur courant introuvable"));

        AppUser friendUser = appUserRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable",
                        "Aucun utilisateur avec ce nom d'utilisateur"));

        FriendshipEntity friendshipEntity = this.findFriendShipEntity(currentUser.getId(), friendUser.getId());
        if (friendshipEntity == null) {
            throw new GenericNotFoundException(404, "Amitié non trouvée",
                    "Aucune amitié existante entre ces utilisateurs");
        }
        friendShipRepository.delete(friendshipEntity);
    }

    public List<ProfileDetailsDto> getFriendsOfUser(String currentUsername) throws GenericNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable",
                        "Utilisateur courant introuvable"));
        List<FriendshipEntity> friendshipEntities = this.getUsersFriends(currentUser.getId());
        List<ProfileDetailsDto> friends = new ArrayList<>();
        for (FriendshipEntity friendshipEntity : friendshipEntities) {
            Long friendId = friendshipEntity.getUserId1().equals(currentUser.getId())
                    ? friendshipEntity.getUserId2()
                    : friendshipEntity.getUserId1();
            // fixme: si l'utilisateur n'existe pas, on l'ignore (car un user peut supprimé
            // son profile)
            Optional<AppUser> friend = appUserRepository.findById(friendId);
            if (friend.isPresent()) {
                Optional<ProfileEntity> profileEntity = profilRepository.findByUserId(friend.get().getId());
                profileEntity.ifPresent(entity -> {
                    try {
                        friends.add(profilService.getProfileByUsername(entity.getUser().getUsername()));
                    } catch (GenericNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        return friends;
    }

    private boolean checkIfFriendShipAlreadyExist(Long userId1, Long userId2) {
        Optional<FriendshipEntity> friendship = this.friendShipRepository.findByUserId1AndUserId2(userId1, userId2);
        if (friendship.isEmpty()) {
            friendship = this.friendShipRepository.findByUserId1AndUserId2(userId2, userId1);
            return friendship.isPresent();
        } else {
            return true;
        }
    }

    /**
     * @param userId2 Username of sender / receiver
     * @param userId1 Username of receiver / sender
     * @return Friendship if it was found, null otherwise
     */
    private FriendshipEntity findFriendShipEntity(Long userId1, Long userId2) {
        Optional<FriendshipEntity> friendship = this.friendShipRepository.findByUserId1AndUserId2(userId1, userId2);
        if (friendship.isEmpty()) {
            friendship = this.friendShipRepository.findByUserId1AndUserId2(userId2, userId1);
            return friendship.orElse(null);
        } else {
            return friendship.get();
        }
    }

    private List<FriendshipEntity> getUsersFriends(Long userId) {
        List<FriendshipEntity> friendshipEntities = new ArrayList<>();
        friendshipEntities.addAll(this.friendShipRepository.findByUserId1(userId));
        friendshipEntities.addAll(this.friendShipRepository.findByUserId2(userId));
        return friendshipEntities;
    }

    public void deleteAllByUserId(Long userId) {
        List<FriendshipEntity> friendshipsToDelete = new ArrayList<>();
        friendshipsToDelete.addAll(friendShipRepository.findByUserId1(userId));
        friendshipsToDelete.addAll(friendShipRepository.findByUserId2(userId));

        friendShipRepository.deleteAll(friendshipsToDelete);
    }

    public boolean isFriend(Long idUser1, Long idUser2) {
        return this.friendShipRepository.findByUserId1AndUserId2(idUser1, idUser2).isPresent()
                || this.friendShipRepository.findByUserId1AndUserId2(idUser2, idUser1).isPresent();
    }

}
