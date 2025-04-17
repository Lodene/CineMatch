package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.ConversationDto;
import fr.cpe.cinematch_backend.dtos.MessageDto;
import fr.cpe.cinematch_backend.dtos.requests.MessageRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ConversationEntity;
import fr.cpe.cinematch_backend.entities.MessageEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MessageMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.ConversationRepository;
import fr.cpe.cinematch_backend.repositories.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FriendshipService friendshipService;

    public List<ConversationDto> getUserConversations(String username) throws GenericNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "Utilisateur introuvable",
                        "Utilisateur courant introuvable"));

        List<ConversationEntity> conversations = conversationRepository.findByFromOrTo(currentUser, currentUser);

        return conversations.stream().map(conversation -> {
            String fromUsername = conversation.getFrom().getUsername();
            String toUsername = conversation.getTo().getUsername();

            List<MessageDto> messageDtos = conversation.getMessages().stream()
                    .map(MessageMapper.INSTANCE::toMessageDto)
                    .collect(Collectors.toList());

            return new ConversationDto(conversation.getId(), fromUsername, toUsername, messageDtos);
        }).collect(Collectors.toList());
    }

    public ConversationDto createConversation(Long fromUserId, Long toUserId) throws GenericNotFoundException {
        if (fromUserId.equals(toUserId)) {
            throw new GenericNotFoundException(400, "Invalid Request", "Cannot create conversation with yourself.");
        }

        AppUser fromUser = appUserRepository.findById(fromUserId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "fromUser not found"));
        AppUser toUser = appUserRepository.findById(toUserId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "toUser not found"));

        Optional<ConversationEntity> existing = conversationRepository.findByFromAndTo(fromUser, toUser);

        if (existing.isPresent()) {
            throw new GenericNotFoundException(409, "Conflict", "Conversation already exists.");
        }

        // Create a new conversation

        ConversationEntity conversation = new ConversationEntity();
        conversation.setFrom(fromUser);
        conversation.setTo(toUser);
        conversation = conversationRepository.save(conversation);

        return new ConversationDto(conversation.getId(), fromUser.getUsername(), toUser.getUsername(), List.of());
    }

    public void sendMessage(Long senderId, MessageRequest request) throws GenericNotFoundException {
        // Récupération du sender et du destinataire
        AppUser sender = appUserRepository.findById(senderId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Sender not found"));
        AppUser receiver = appUserRepository.findByUsername(request.getToUsername())
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "Receiver not found"));

        // Vérification de la validité du message
        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new GenericNotFoundException(400, "Invalid Request", "Message content cannot be empty.");
        }
        // Empêcher d’envoyer un message à soi-même
        if (sender.getId() == receiver.getId()) {
            throw new GenericNotFoundException(400, "Invalid Request", "Cannot send message to yourself.");
        }

        // Vérification d’amitié
        if (!friendshipService.isFriend(sender.getId(), receiver.getId())) {
            throw new GenericNotFoundException(403, "Access denied", "You must be friends to send a message");
        }

        // Vérifie si une conversation existe déjà (dans les deux sens)
        Optional<ConversationEntity> optionalConversation = conversationRepository.findByFromAndTo(sender, receiver);

        if (optionalConversation.isEmpty()) {
            optionalConversation = conversationRepository.findByFromAndTo(receiver, sender);
        }

        ConversationEntity conversation = optionalConversation.orElseGet(() -> {
            // Créer la conversation si elle n’existe pas
            ConversationEntity newConversation = new ConversationEntity();
            newConversation.setFrom(sender);
            newConversation.setTo(receiver);
            return conversationRepository.save(newConversation);
        });

        // Enregistrement du message
        MessageEntity message = new MessageEntity();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }

    public void deleteAllConversationsAndMessagesByUserId(Long userId) throws GenericNotFoundException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with ID " + userId + " not found"));

        // Supprimer tous les messages envoyés par cet utilisateur
        List<MessageEntity> messages = messageRepository.findBySender(user);
        messageRepository.deleteAll(messages);

        // Supprimer toutes les conversations où il est participant
        List<ConversationEntity> conversations = conversationRepository.findByFromOrTo(user, user);
        conversationRepository.deleteAll(conversations);
    }

}
