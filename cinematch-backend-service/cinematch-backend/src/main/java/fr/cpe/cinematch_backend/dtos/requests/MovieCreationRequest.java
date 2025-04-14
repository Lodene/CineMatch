package fr.cpe.cinematch_backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieCreationRequest {
    private String title;
    private String overview;
    private Date releaseDate;
    private String posterPath;
    private List<String> genres;
    private float imdb_rating;
}
