package fr.cpe.cinematch_backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ConfigErrorException extends Exception {

    private int status;
    private String reason;

    public ConfigErrorException(int status, String message, String reason) {
            super(message);
            this.status = status;
            this.reason = reason;
        }
    }
