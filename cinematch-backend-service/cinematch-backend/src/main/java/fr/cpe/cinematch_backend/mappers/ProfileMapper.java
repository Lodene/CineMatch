package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

@Mapper
public interface ProfileMapper {

ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    // fixme: use constructor with mapperResolver to automaticaly retrieve user based on profile username
    @Mapping(target = "id", ignore = true)
    ProfileEntity toProfileEntity(ProfileDto profileDto);

    @Mapping(target = "username", expression = "java(profileEntity.getUser().getUsername())")
    @Mapping(target = "isChild", source = "child")
    ProfileDto toProfileDto(ProfileEntity profileEntity);

}
