package fr.cpe.cinematch_backend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericNotFoundException extends Exception {
    private int status;
    private String reason;
    public GenericNotFoundException(int status, String message, String reason) {
        super(message);
        this.status = status;
        this.reason = reason;
    }
}
