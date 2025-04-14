package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomingFriendRequestDto {
    private Long requestId;
    private String username;
    private String profilPicture;
}
