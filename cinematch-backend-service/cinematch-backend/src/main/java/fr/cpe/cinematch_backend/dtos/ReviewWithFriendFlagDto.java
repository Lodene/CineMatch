package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithFriendFlagDto extends ReviewDto {
    private boolean writtenByFriend;
    private ProfileDto userProfile;
}
