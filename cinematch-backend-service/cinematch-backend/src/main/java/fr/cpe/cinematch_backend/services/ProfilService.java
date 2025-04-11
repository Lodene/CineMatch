package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ProfileMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import fr.cpe.cinematch_backend.services.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    public ProfileDto getCurrentUserProfile() throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile();
        return ProfileMapper.INSTANCE.toProfileDto(profileEntity);
    }

    public void updateProfile(ProfileDto dto) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile();
        profileEntity.setDescription(dto.getDescription());
        profileEntity.setChild(dto.isChild());
        if (dto.getProfilPicture() != null) {
            profileEntity.setProfilPicture(dto.getProfilPicture());
        }
        profilRepository.save(profileEntity);
    }

    public void updateProfilePicture(String path) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile();
        profileEntity.setProfilPicture(path);
        profilRepository.save(profileEntity);
    }

    public void deleteProfilePicture() throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile();
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

    public ProfileDto getProfileByUsername(String username) throws GenericNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<ProfileEntity> profileEntity = profilRepository.findByUserId(appUser.get().getId());
            if (profileEntity.isPresent()) {
                return ProfileMapper.INSTANCE.toProfileDto(profileEntity.get());
            }
            throw new GenericNotFoundException(404, "Profile not found", "Profile could not be loaded. Maybe it has been deleted");
        }
        throw  new GenericNotFoundException(404, "Profile not found", "User not found");
    }

    private ProfileEntity checkAndRetrieveProfile() throws GenericNotFoundException {
        AppUser user = currentUserService.getCurrentUser();
        if (user == null) {
            throw new GenericNotFoundException(404,  "Profile not found", "The current user is not logged in or does not exist");
        }
        Optional <ProfileEntity> profileEntity = profilRepository.findByUserId(user.getId());
        if (profileEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Profile not found","Profile with username: '" + user.getUsername() + "' not found");
        }
        return profileEntity.get();
    }

}
