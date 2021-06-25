package io.axoniq.labs.chat.coreapi;

public class RoomCreatedEvent {

    private String roomId;
    private String name;

    public RoomCreatedEvent(String roomId, String name) {
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
