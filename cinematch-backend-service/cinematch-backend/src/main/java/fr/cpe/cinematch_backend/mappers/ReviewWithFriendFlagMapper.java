package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.dtos.ReviewWithFriendFlagDto;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.dtos.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ImageMapper.class)
public interface ReviewWithFriendFlagMapper {

    ReviewWithFriendFlagMapper INSTANCE = Mappers.getMapper(ReviewWithFriendFlagMapper.class);

    @Mapping(source = "profile", target = "userProfile")
    @Mapping(source = "base.id", target = "id")
    @Mapping(source = "base.idMovie", target = "idMovie")
    @Mapping(source = "base.note", target = "note")
    @Mapping(source = "base.description", target = "description")
    @Mapping(source = "base.createdAt", target = "createdAt")
    @Mapping(source = "base.modifiedAt", target = "modifiedAt")
    @Mapping(target = "writtenByFriend", ignore = true)
    @Mapping(target = "username", source = "profile.username")
    ReviewWithFriendFlagDto fromReviewDtoAndProfile(ReviewDto base, ProfileDto profile);

    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "profilPicture", source = "profilPicture", qualifiedByName = "bytesToBase64")
    ProfileDto profileEntityToDto(ProfileEntity entity);
}
