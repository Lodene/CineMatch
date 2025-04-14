package fr.cpe.cinematch_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "friend_user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id_1", "user_id_2"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id_1")
    private Long userId1;

    @Column(name = "user_id_2")
    private Long userId2;

    @CreationTimestamp
    private Date createdAt;
}
