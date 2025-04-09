package fr.cpe.cinematch_backend.services.security;

import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    public Authentication authenticate(UserRequest userRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );
    }
}