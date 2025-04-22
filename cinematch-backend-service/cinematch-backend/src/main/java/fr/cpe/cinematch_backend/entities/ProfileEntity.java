package fr.cpe.cinematch_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

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

    @Column(name = "profil_picture", columnDefinition = "BYTEA")
    @JdbcTypeCode(Types.BINARY)
    private byte[] profilPicture;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    private AppUser user;
}
