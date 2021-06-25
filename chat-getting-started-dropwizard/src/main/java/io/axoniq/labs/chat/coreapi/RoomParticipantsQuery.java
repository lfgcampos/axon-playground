package io.axoniq.labs.chat.coreapi;

public class RoomParticipantsQuery {

    private String roomId;

    public RoomParticipantsQuery(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
