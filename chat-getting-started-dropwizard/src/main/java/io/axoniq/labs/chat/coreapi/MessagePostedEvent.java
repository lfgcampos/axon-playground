package io.axoniq.labs.chat.coreapi;

public class MessagePostedEvent {

    private String roomId;
    private String participant;
    private String message;

    public MessagePostedEvent(String roomId, String participant, String message) {
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
