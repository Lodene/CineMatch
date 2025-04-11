package fr.cpe.cinematch_backend.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private Long idMovie;
    private Integer note;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
