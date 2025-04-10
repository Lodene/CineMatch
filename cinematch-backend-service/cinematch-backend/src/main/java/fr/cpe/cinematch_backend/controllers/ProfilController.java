package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.ProfilDTO;
import fr.cpe.cinematch_backend.services.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profil")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @GetMapping
    public ResponseEntity<ProfilDTO> getProfil() {
        return ResponseEntity.ok(profilService.getCurrentUserProfil());
    }

    @PutMapping
    public ResponseEntity<Void> updateProfil(@RequestBody ProfilDTO dto) {
        profilService.updateProfil(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/picture")
    public ResponseEntity<Void> updatePicture(@RequestBody String path) {
        profilService.updateProfilePicture(path);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> deletePicture() {
        profilService.deleteProfilePicture();
        return ResponseEntity.ok().build();
    }
}
