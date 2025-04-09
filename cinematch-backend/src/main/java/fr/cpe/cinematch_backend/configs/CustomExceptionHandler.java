package fr.cpe.cinematch_backend.configs;

import fr.cpe.cinematch_backend.dtos.ErrorDto;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler
{
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto processMalformedJwtException(MalformedJwtException e) {
        return new ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), "The token is invalid");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto processExpiredJwtException(ExpiredJwtException e) {

        return new ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), "The token has expired");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto processAuthenticationException(AuthenticationException e) {
        return new ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), "Invalid credentials");
    }
    @ExceptionHandler(BadEndpointException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto processBadEndpointException(BadEndpointException e) {
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getReason());
    }
}