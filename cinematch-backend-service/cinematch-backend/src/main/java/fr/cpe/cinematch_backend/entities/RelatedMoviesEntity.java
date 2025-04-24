package fr.cpe.cinematch_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "related_movies")
public class RelatedMoviesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie1_id", nullable = false)
    private MovieEntity movie1;

    @ManyToOne
    @JoinColumn(name = "movie2_id", nullable = false)
    private MovieEntity movie2;
}
