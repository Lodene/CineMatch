package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.MessageDto;
import fr.cpe.cinematch_backend.entities.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "sender.username", target = "senderUsername")
    MessageDto toMessageDto(MessageEntity message);
}