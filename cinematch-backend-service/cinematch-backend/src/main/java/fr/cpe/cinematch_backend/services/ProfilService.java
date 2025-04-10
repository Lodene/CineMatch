package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ProfilDTO;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.Profil;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
import fr.cpe.cinematch_backend.services.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    public ProfilDTO getCurrentUserProfil() {
        AppUser user = currentUserService.getCurrentUser();
        Profil profil = profilRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Profil not found for current user"));
        return new ProfilDTO(profil.isChild(), profil.getDescription(), profil.getProfilPicture());
    }

    public void updateProfil(ProfilDTO dto) {
        AppUser user = currentUserService.getCurrentUser();
        Profil profil = profilRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Profil not found for current user"));
        profil.setDescription(dto.getDescription());
        profil.setChild(dto.isChild());
        profilRepository.save(profil);
    }

    public void updateProfilePicture(String path) {
        AppUser user = currentUserService.getCurrentUser();
        Profil profil = profilRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Profil not found for current user"));
        profil.setProfilPicture(path);
        profilRepository.save(profil);
    }

    public void deleteProfilePicture() {
        AppUser user = currentUserService.getCurrentUser();
        Profil profil = profilRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Profil not found for current user"));
        profil.setProfilPicture(null);
        profilRepository.save(profil);
    }

    public void createProfilForUser(AppUser user) {
        Profil profil = new Profil();
        profil.setUser(user);
        profil.setChild(false); // par défaut
        profil.setDescription("");
        profil.setProfilPicture(null);
        profilRepository.save(profil);
    }

    public ProfilDTO getProfilByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + username));

        Profil profil = profilRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Profil not found for username: " + username));

        return new ProfilDTO(profil.isChild(), profil.getDescription(), profil.getProfilPicture());
    }
}
