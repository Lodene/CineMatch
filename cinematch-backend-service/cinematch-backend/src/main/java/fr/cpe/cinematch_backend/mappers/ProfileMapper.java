package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ImageMapper.class)
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilPicture", source = "profilPicture", qualifiedByName = "base64ToBytes")
    ProfileEntity toProfileEntity(ProfileDto profileDto);

    @Mapping(target = "username", expression = "java(profileEntity.getUser().getUsername())")
    @Mapping(target = "isChild", source = "child")
    @Mapping(target = "profilPicture", source = "profilPicture", qualifiedByName = "bytesToBase64")
    ProfileDto toProfileDto(ProfileEntity profileEntity);
}
