package io.axoniq.labs.chat.query.rooms.messages;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = "io.axoniq.labs.chat.query.rooms.messages.ChatMessage.findAllByRoomIdOrderByTimestamp",
                        query = "SELECT c FROM ChatMessage c WHERE c.roomId = :roomId ORDER BY c.timestamp"
                )
        }
)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long timestamp;
    private String roomId;
    private String message;
    private String participant;

    public ChatMessage() {
    }

    public ChatMessage(String participant, String roomId, String message, long timestamp) {
        this.participant = participant;
        this.roomId = roomId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getParticipant() {
        return participant;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", roomId='" + roomId + '\'' +
                ", message='" + message + '\'' +
                ", participant='" + participant + '\'' +
                '}';
    }
}
