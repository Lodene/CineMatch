package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ProfileMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public ProfileDto getProfileByUsername(String username) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        return ProfileMapper.INSTANCE.toProfileDto(profileEntity);
    }

    public void updateProfile(String username, ProfileDto dto) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        profileEntity.setDescription(dto.getDescription());
        profileEntity.setChild(dto.isChild());
        if (dto.getProfilPicture() != null) {
            profileEntity.setProfilPicture(dto.getProfilPicture());
        }
        profilRepository.save(profileEntity);
    }

    public void updateProfilePicture(String username, String path) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        profileEntity.setProfilPicture(path);
        profilRepository.save(profileEntity);
    }

    public void deleteProfilePicture(String username) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        profileEntity.setProfilPicture(null);
        profilRepository.save(profileEntity);
    }

    public void createProfileForUser(AppUser user) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUser(user);
        profileEntity.setChild(false); // par défaut
        profileEntity.setDescription("");
        profileEntity.setProfilPicture(null);
        profilRepository.save(profileEntity);
    }

    private ProfileEntity checkAndRetrieveProfile(String username) throws GenericNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "Username not found in DB");
        }
        Optional<ProfileEntity> profileEntity = profilRepository.findByUserId(user.get().getId());
        if (profileEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Profile not found", "Profile for user not found");
        }
        return profileEntity.get();
    }

    public ProfileEntity deleteProfil(AppUser user) throws GenericNotFoundException {
        Optional<ProfileEntity> profileEntity = profilRepository.findById(user.getId());
        if (profileEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Profile not found", "Profile for user not found");
        }
        profilRepository.delete(profileEntity.get());
        return profileEntity.get();
    }
}
