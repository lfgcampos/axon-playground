package io.axoniq.labs.chat.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class JoinRoomCommand {

    @TargetAggregateIdentifier
    private String roomId;
    private String participant;

    public JoinRoomCommand(String roomId, String participant) {
        this.roomId = roomId;
        this.participant = participant;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getParticipant() {
        return participant;
    }
}
