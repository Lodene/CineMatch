package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
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
    public ResponseEntity<ProfileDto> getProfil() throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getCurrentUserProfile());
    }

    @PutMapping
    public ResponseEntity<Void> updateProfil(@RequestBody ProfileDto dto) throws GenericNotFoundException {
        profilService.updateProfile(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/picture")
    public ResponseEntity<Void> updatePicture(@RequestBody String path) throws GenericNotFoundException {
        profilService.updateProfilePicture(path);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> deletePicture() throws GenericNotFoundException {
        profilService.deleteProfilePicture();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getProfilByUsername(@PathVariable String username) throws GenericNotFoundException {
        return ResponseEntity.ok(profilService.getProfileByUsername(username));
    }

}
