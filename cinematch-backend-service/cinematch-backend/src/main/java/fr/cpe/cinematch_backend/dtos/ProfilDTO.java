package fr.cpe.cinematch_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilDTO {
    private boolean isChild;
    private String description;
    private String profilPicture;
}
