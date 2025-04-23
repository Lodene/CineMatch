package fr.cpe.cinematch_backend.services.security;

import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.cpe.cinematch_backend.configs.SecurityConfig;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Authentication authenticate(UserRequest userRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()));
    }

    public void updatePassword(String username, String password) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "User does not exist"));

        // Validation du mot de passe
        if (password.length() < 8 || password.length() > 12) {
            throw new GenericNotFoundException(400, "Invalid Password", "Password must be between 8 and 12 characters");
        }

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new GenericNotFoundException(400, "Invalid Password",
                    "Password must contain at least one uppercase letter");
        }

        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new GenericNotFoundException(400, "Invalid Password", "Password must contain at least one digit");
        }

        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            throw new GenericNotFoundException(400, "Invalid Password",
                    "Password must contain at least one special character");
        }

        // Encodage du mot de passe
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        appUserRepository.save(user);
    }
}