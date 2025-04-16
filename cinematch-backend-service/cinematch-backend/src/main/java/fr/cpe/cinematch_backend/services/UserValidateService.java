package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.requests.UserRequest;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserValidateService {

    @Transactional
    public boolean validateUser(UserRequest userRequest) throws BadEndpointException {

        List<String> validationErrors = userRequest.validate();
        if (!validationErrors.isEmpty()) {
            throw new BadEndpointException(400, "Failed to create new account",
                String.join("; ", validationErrors));
        }
        return true;
    }
}
