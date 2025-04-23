package fr.cpe.cinematch_backend.configs;

import fr.cpe.cinematch_backend.dtos.ErrorDto;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.ConfigErrorException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
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

    @ExceptionHandler(GenericNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto processGenericNotFoundException(GenericNotFoundException e) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage(), e.getReason());
    }

    @ExceptionHandler(ConfigErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto processConfigErrorException(ConfigErrorException e) {
        return new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getReason());
    }
}