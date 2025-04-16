package fr.cpe.cinematch_backend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewWithFriendFlagDto extends ReviewDto {
    private boolean writtenByFriend;
    private ProfileDto userProfile;
}
