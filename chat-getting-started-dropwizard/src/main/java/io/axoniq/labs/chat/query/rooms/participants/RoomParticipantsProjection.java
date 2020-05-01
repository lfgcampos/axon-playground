package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery;
import io.dropwizard.hibernate.UnitOfWork;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@ProcessingGroup("projection")
public class RoomParticipantsProjection {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RoomParticipantsDAO repository;

    public RoomParticipantsProjection(RoomParticipantsDAO repository) {
        this.repository = repository;
    }

    @EventHandler
    @UnitOfWork
    public void on(ParticipantJoinedRoomEvent event) {
        repository.save(new RoomParticipant(event.getRoomId(), event.getParticipant()));
    }

    @EventHandler
    @UnitOfWork
    public void on(ParticipantLeftRoomEvent event) {
        repository.deleteByParticipantAndRoomId(event.getParticipant(), event.getRoomId());
    }

    @QueryHandler
    @UnitOfWork
    public List<String> handle(RoomParticipantsQuery query) {
        return repository.findRoomParticipantsByRoomId(query.getRoomId())
                         .stream()
                         .map(RoomParticipant::getParticipant)
                         .collect(Collectors.toList());
    }

    @ResetHandler
    @UnitOfWork
    public void onReset() {
        logger.info("Handling ResetTriggeredEvent on RoomParticipantsProjection");
        repository.deleteAll();
    }
}
