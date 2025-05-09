package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserConfigurationService userConfigurationService;

    @Autowired
    private ProfilService profilService;

    @Transactional
    public boolean insertUser(UserRequest userRequest) throws BadEndpointException, GenericNotFoundException {

        if (appUserRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new BadEndpointException(403, "Failed to create new account", "Username is already in use");
        }

        // Check if email already exists (recommended additional validation)
        if (appUserRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new BadEndpointException(403, "Failed to create new account", "Email address is already in use");
        }

        AppUser userEntity = new AppUser();
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(userRequest.getPassword());
        appUserRepository.save(userEntity);
        profilService.createProfileForUser(userEntity);

        userConfigurationService.createUserConfiguration(userEntity.getId(), "en");
        return true;
    }

    public boolean deleteUserById(AppUser user) throws BadEndpointException {
        if (user == null) {
            throw new BadEndpointException(403, "Failed to delete account", "User not found");
        }
        appUserRepository.delete(user);
        return true;
    }
}
