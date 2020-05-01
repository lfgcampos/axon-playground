package io.axoniq.labs.chat.query.rooms.participants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"roomId", "participant"}))
@NamedQueries(
        {
                @NamedQuery(
                        name = "io.axoniq.labs.chat.query.rooms.participants.RoomParticipant.findRoomParticipantsByRoomId",
                        query = "SELECT r FROM RoomParticipant r WHERE r.roomId = :roomId"
                ),
                @NamedQuery(
                        name = "io.axoniq.labs.chat.query.rooms.participants.RoomParticipant.findByParticipantAndRoomId",
                        query = "SELECT r FROM RoomParticipant r WHERE r.participant = :participant AND r.roomId = :roomId"
                )
        }
)
public class RoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private String participant;

    public RoomParticipant() {
    }

    public RoomParticipant(String roomId, String participant) {
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
