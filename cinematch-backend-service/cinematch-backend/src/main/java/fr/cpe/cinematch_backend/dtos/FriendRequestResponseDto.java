package fr.cpe.cinematch_backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestResponseDto {
    private Long requestId;
    private boolean accepted;
}
