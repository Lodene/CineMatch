package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.Profil;
import fr.cpe.cinematch_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByUserId(Long userId);
}
