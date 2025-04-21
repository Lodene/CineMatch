package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.UserConfiguration;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.UserPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserConfigurationService {

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public void updateLanguagePreference(String username, String lang)
            throws GenericNotFoundException {
        if (!lang.equalsIgnoreCase("fr")  && !lang.equalsIgnoreCase( "en")) {
            throw new GenericNotFoundException(400, "Invalid language", "Only 'en' or 'fr' are allowed.");
        }

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "User does not exist"));

        Optional<UserConfiguration> optionalPref = userPreferencesRepository.findByUser(user);
        UserConfiguration pref = optionalPref.orElse(new UserConfiguration());
        pref.setLang(lang);
        pref.setUser(user);
        userPreferencesRepository.save(pref);
    }

    public String getUserLanguage(String username) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "User does not exist"));

        // Check if the user has a configuration and throw error if not
        if (userPreferencesRepository.findByUser(user).isEmpty()) {
            throw new GenericNotFoundException(404, "User configuration not found",
                    "User configuration does not exist");
        }
        Optional<UserConfiguration> userConfig = userPreferencesRepository.findByUser(user);
        return userConfig.isPresent() ? userConfig.get().getLang() : "";
    }

    public void createUserConfiguration(Long userId, String lang) throws GenericNotFoundException {
        if (!(lang.equals("fr") || lang.equals("en") || lang.isEmpty())) {
            throw new GenericNotFoundException(400, "Invalid language", "Only 'en' or 'fr' are allowed.");
        }
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "user with id: '" + userId + "' not found"));
        Optional<UserConfiguration> existing = userPreferencesRepository.findByUser(user);
        if (existing.isEmpty()) {
            UserConfiguration preference = new UserConfiguration();
            preference.setUser(user);
            preference.setLang(lang); // valeur par défaut
            userPreferencesRepository.save(preference);
        }
    }
}
