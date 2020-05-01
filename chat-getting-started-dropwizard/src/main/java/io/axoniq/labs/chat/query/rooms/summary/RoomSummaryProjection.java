package io.axoniq.labs.chat.query.rooms.summary;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import io.dropwizard.hibernate.UnitOfWork;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@ProcessingGroup("projection")
public class RoomSummaryProjection {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RoomSummaryDAO repository;

    public RoomSummaryProjection(RoomSummaryDAO repository) {
        this.repository = repository;
    }

    @EventHandler
    @UnitOfWork
    public void on(RoomCreatedEvent event) {
        repository.save(new RoomSummary(event.getRoomId(), event.getName()));
    }

    @EventHandler
    @UnitOfWork
    public void on(ParticipantJoinedRoomEvent event) {
        repository.findById(event.getRoomId()).ifPresent(roomSummary -> {
            roomSummary.addParticipant();
            repository.save(roomSummary);
        });
    }

    @EventHandler
    @UnitOfWork
    public void on(ParticipantLeftRoomEvent event) {
        repository.findById(event.getRoomId()).ifPresent(roomSummary -> {
            roomSummary.removeParticipant();
            repository.save(roomSummary);
        });
    }

    @QueryHandler
    @UnitOfWork
    public List<RoomSummary> handle(AllRoomsQuery query) {
        return repository.findAll();
    }

    @ResetHandler
    @UnitOfWork
    public void onReset() {
        logger.info("Handling ResetTriggeredEvent on RoomSummaryProjection");
        repository.deleteAll();
    }
}
