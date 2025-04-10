package fr.cpe.cinematch_backend.services.security;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private HttpServletRequest request;

    public AppUser getCurrentUser() {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return appUserRepository.findByUsername(username).orElse(null);
    }
}
