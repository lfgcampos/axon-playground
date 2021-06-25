package io.axoniq.labs.chat.coreapi;

public class ParticipantJoinedRoomEvent {

    private String roomId;
    private String participant;

    public ParticipantJoinedRoomEvent(String roomId, String participant) {
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
