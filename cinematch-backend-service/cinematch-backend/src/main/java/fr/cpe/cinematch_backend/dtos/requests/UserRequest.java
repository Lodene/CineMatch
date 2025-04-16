package fr.cpe.cinematch_backend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;

    private String password;

    private String email;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Username validation pattern
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+$");

    // Password validation pattern
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$");

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        // Username validation
        if (username == null || username.trim().isEmpty()) {
            errors.add("Username cannot be empty");
        } else if (username.length() < 3 || username.length() > 50) {
            errors.add("Username must be between 3 and 50 characters");
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.add("Username can only contain letters, numbers, periods, underscores, and hyphens");
        }

        // Password validation
        if (password == null || password.trim().isEmpty()) {
            errors.add("Password cannot be empty");
        } else if (password.length() < 8 || password.length() > 12) {
            errors.add("Password must be between 8 and 12 characters");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.add("Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character");
        }

        // Email validation
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email cannot be empty");
        } else if (email.length() > 100) {
            errors.add("Email must not exceed 100 characters");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email must be valid");
        }

        return errors;
    }

}
