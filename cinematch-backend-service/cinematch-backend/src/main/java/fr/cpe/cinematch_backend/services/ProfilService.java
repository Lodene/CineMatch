package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.dtos.ProfileDetailsDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ProfileMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private WatchlistMovieService watchlistMovieService;

    @Autowired
    private WatchedMovieService watchedMovieService;

    public ProfileDetailsDto  getProfileByUsername(String username) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        AppUser user = profileEntity.getUser();

        int reviewsCount = reviewService.getUserReviews(username).size();
        int watchlistCount = watchlistMovieService.getWatchlistByUsername(username).size();
        int watchedMoviesCount = watchedMovieService.getWatchedMoviesByUsername(username).size();

        ProfileDetailsDto profileDetailsDto = new ProfileDetailsDto();
        profileDetailsDto.setDescription(profileEntity.getDescription());
        profileDetailsDto.setUsername(profileEntity.getUser().getUsername());
        profileDetailsDto.setProfilPicture(profileEntity.getProfilPicture());
        profileDetailsDto.setChild(profileEntity.isChild());
        profileDetailsDto.setReviewsCount(reviewsCount);
        profileDetailsDto.setWatchlistCount(watchlistCount);
        profileDetailsDto.setWatchedMoviesCount(watchedMoviesCount);

        return profileDetailsDto;
    }

    public ProfileDetailsDto getProfileById(Long id) throws GenericNotFoundException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "User with id: '" + id + "' was not found");
        }
        return this.getProfileByUsername(user.get().getUsername());
    }

    public void updateProfile(String username, ProfileDto dto) throws GenericNotFoundException {
        ProfileEntity e = checkAndRetrieveProfile(username);

        // on ne touche plus du tout à la photo ici
        e.setDescription(dto.getDescription());
        e.setChild(dto.isChild());

        profilRepository.save(e);
    }

    public void deleteProfilePicture(String username) throws GenericNotFoundException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        profileEntity.setProfilPicture(new byte[0]);
        profilRepository.save(profileEntity);
    }

    public void saveProfilePicture(String username, MultipartFile file) throws GenericNotFoundException, IOException {
        ProfileEntity profileEntity = this.checkAndRetrieveProfile(username);
        profileEntity.setProfilPicture(file.getBytes());
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

    public List<ProfileDetailsDto> getAllProfiles() {
        List<ProfileEntity> profiles = profilRepository.findAll();
        return profiles.stream()
                .map(entity ->
                {
                    try {
                        return this.getProfileById(entity.getId());
                    } catch (GenericNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
