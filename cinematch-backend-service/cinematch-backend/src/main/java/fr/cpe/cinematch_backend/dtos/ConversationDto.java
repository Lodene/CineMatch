package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private Long conversationId;
    private String fromUsername;
    private String toUsername;
    private List<MessageDto> messages;
}