package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailsWithReviewsDto {
    private MovieDto movie;
    private boolean isCommented;
    private boolean isLoved;
    private boolean isInWatchlist;
    private List<ReviewWithFriendFlagDto> reviews;
}