package fr.cpe.cinematch_backend.dtos.requests;

import lombok.Data;

@Data
public class MessageRequest {
    private String toUsername;
    private String content;
}
