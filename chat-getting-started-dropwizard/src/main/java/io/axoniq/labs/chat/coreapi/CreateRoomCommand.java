package io.axoniq.labs.chat.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateRoomCommand {

    @TargetAggregateIdentifier
    private String roomId;
    private String name;

    public CreateRoomCommand(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
}
