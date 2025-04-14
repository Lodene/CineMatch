package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendDTO {
    private String username;
    private String profilPicture;
}
