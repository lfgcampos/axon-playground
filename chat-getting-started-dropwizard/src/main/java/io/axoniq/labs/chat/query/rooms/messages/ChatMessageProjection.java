package io.axoniq.labs.chat.query.rooms.messages;

import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery;
import io.dropwizard.hibernate.UnitOfWork;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;

@ProcessingGroup("projection")
public class ChatMessageProjection {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ChatMessageDAO repository;
    private QueryUpdateEmitter updateEmitter;

    public ChatMessageProjection(ChatMessageDAO repository) {
        this.repository = repository;
    }

    public void setUpdateEmitter(QueryUpdateEmitter updateEmitter) {
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    @UnitOfWork
    public void on(MessagePostedEvent event, @Timestamp Instant timestamp) {
        ChatMessage chatMessage = new ChatMessage(event.getParticipant(),
                                                  event.getRoomId(),
                                                  event.getMessage(),
                                                  timestamp.toEpochMilli());
        repository.save(chatMessage);
        updateEmitter.emit(RoomMessagesQuery.class, query -> query.getRoomId().equals(event.getRoomId()), chatMessage);
    }

    @QueryHandler
    @UnitOfWork
    public List<ChatMessage> handle(RoomMessagesQuery query) {
        return repository.findAllByRoomIdOrderByTimestamp(query.getRoomId());
    }

    @ResetHandler
    @UnitOfWork
    public void onReset() {
        logger.info("Handling ResetTriggeredEvent on ChatMessageProjection");
        repository.deleteAll();
    }
}
