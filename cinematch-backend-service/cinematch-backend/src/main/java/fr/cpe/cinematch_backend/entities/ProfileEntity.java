package fr.cpe.cinematch_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChild;

    private String description;

    private String profilPicture; // Path vers l'image

    @OneToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id", nullable = false)
    private AppUser user;
}
