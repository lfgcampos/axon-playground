package io.axoniq.labs.chat.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class LeaveRoomCommand {

    @TargetAggregateIdentifier
    private String roomId;
    private String participant;

    public LeaveRoomCommand(String roomId, String participant) {
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
