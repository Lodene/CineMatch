package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.LoginResponse;
import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.services.AppUserService;
import fr.cpe.cinematch_backend.services.UserConfigurationService;
import fr.cpe.cinematch_backend.services.UserValidateService;
import fr.cpe.cinematch_backend.services.security.AuthenticationService;
import fr.cpe.cinematch_backend.services.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService userService;
    @Autowired
    private UserValidateService userValidateService;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    @Autowired
    private UserConfigurationService userConfigurationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest, HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, GenericNotFoundException {
        Authentication authenticatedUser = authenticationService.authenticate(userRequest);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticatedUser);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        String jwtToken = jwtService.generateToken((UserDetails) authenticatedUser.getPrincipal());

        AppUser uE = (AppUser) authenticatedUser.getPrincipal();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setLang(userConfigurationService.getUserLanguage(userRequest.getUsername()));
        loginResponse.setUsername(userRequest.getUsername());
        loginResponse.setFirstConnexion(uE.isFirstConnexion());
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

    // modify password
    @PutMapping("/password/{password}")
    public ResponseEntity<Void> updatePassword(@PathVariable String password)
            throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        authenticationService.updatePassword(user.getUsername(), password);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/first-connexion/disable")
    public ResponseEntity<Void> disableFirstConnexion() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();

        AppUser appUser;
        appUser = appUserRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "username '" + user.getUsername() + "' does not exist"));
        if (!user.isFirstConnexion()) {
            return ResponseEntity.badRequest().build(); // on ne peut pas repasser à true
        }

        user.setFirstConnexion(false);
        appUserRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
