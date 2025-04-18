package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.LoginResponse;
import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.AppUserService;
import fr.cpe.cinematch_backend.services.UserValidateService;
import fr.cpe.cinematch_backend.services.security.AuthenticationService;
import fr.cpe.cinematch_backend.services.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AppUserService userService;
    @Autowired
    private UserValidateService userValidateService;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest, HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        Authentication authenticatedUser = authenticationService.authenticate(userRequest);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticatedUser);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        String jwtToken = jwtService.generateToken((UserDetails) authenticatedUser.getPrincipal());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserRequest> signup(@RequestBody UserRequest userRequest)
            throws BadEndpointException, GenericNotFoundException {

        boolean valid = userValidateService.validateUser(userRequest);
        if (!valid) {
            ResponseEntity.badRequest();
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        boolean res = userService.insertUser(userRequest);
        if (!res) {
            ResponseEntity.badRequest();
        }

        return ResponseEntity.ok().build();
    }
}
