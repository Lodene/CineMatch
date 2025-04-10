package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private Date releaseDate;
    private String poster;
}
