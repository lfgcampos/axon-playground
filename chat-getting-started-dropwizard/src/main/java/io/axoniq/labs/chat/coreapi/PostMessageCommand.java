package io.axoniq.labs.chat.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class PostMessageCommand {

    @TargetAggregateIdentifier
    private String roomId;
    private String participant;
    private String message;

    public PostMessageCommand(String roomId, String participant, String message) {
        this.roomId = roomId;
        this.participant = participant;
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getParticipant() {
        return participant;
    }

    public String getMessage() {
        return message;
    }
}
