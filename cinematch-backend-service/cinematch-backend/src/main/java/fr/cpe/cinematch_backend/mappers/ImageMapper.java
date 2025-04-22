package fr.cpe.cinematch_backend.mappers;

import org.mapstruct.Named;

import java.util.Base64;

public class ImageMapper {

    @Named("bytesToBase64")
    public static String bytesToBase64(byte[] image) {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }

    @Named("base64ToBytes")
    public static byte[] base64ToBytes(String base64) {
        return base64 != null ? Base64.getDecoder().decode(base64) : null;
    }
}
