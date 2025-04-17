package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
}
