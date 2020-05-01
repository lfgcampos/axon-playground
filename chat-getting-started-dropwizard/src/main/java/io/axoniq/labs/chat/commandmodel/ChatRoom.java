package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.JoinRoomCommand;
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand;
import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.PostMessageCommand;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

public class ChatRoom {

    @AggregateIdentifier
    String roomId;

    Set<String> participants = new HashSet<>();

    ChatRoom() {

    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand command) {
        apply(new RoomCreatedEvent(command.getRoomId(), command.getName()));
    }

    @CommandHandler
    public void handle(JoinRoomCommand command) {
        if (!hasJoined(command.getParticipant())) {
            apply(new ParticipantJoinedRoomEvent(command.getRoomId(), command.getParticipant()));
        }
    }

    @CommandHandler
    public void handle(LeaveRoomCommand command) {
        if (hasJoined(command.getParticipant())) {
            apply(new ParticipantLeftRoomEvent(command.getRoomId(), command.getParticipant()));
        }
    }

    @CommandHandler
    public void handle(PostMessageCommand command) {
        if (!hasJoined(command.getParticipant())) {
            throw new IllegalStateException("You are not part of this room.");
        }
        apply(new MessagePostedEvent(command.getRoomId(), command.getParticipant(), command.getMessage()));
    }

    private boolean hasJoined(String participant) {
        return participants.contains(participant);
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent event) {
        this.roomId = event.getRoomId();
    }

    @EventSourcingHandler
    public void on(ParticipantJoinedRoomEvent event) {
        participants.add(event.getParticipant());
    }

    @EventSourcingHandler
    public void on(ParticipantLeftRoomEvent event) {
        participants.remove(event.getParticipant());
    }
}
