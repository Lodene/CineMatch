package fr.cpe.cinematch_backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieCreationRequest {
    private String title;
    private String description;
    private Date releaseDate;
    private String poster;

}
