package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetailsDto {
    private String username;
    private String description;
    private byte[] profilPicture;
    private boolean isChild;

    private int reviewsCount;
    private int watchlistCount;
    private int watchedMoviesCount;
}

