package io.axoniq.labs.chat.query.rooms.participants;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class RoomParticipantsDAO extends AbstractDAO<RoomParticipant> {

    public RoomParticipantsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    List<RoomParticipant> findRoomParticipantsByRoomId(String roomId) {
        return list((Query<RoomParticipant>)
                            namedQuery(
                                    "io.axoniq.labs.chat.query.rooms.participants.RoomParticipant.findRoomParticipantsByRoomId")
                                    .setParameter("roomId", roomId)
        );
    }

    void deleteByParticipantAndRoomId(String participant, String roomId) {
        RoomParticipant roomParticipant = uniqueResult((Query<RoomParticipant>) namedQuery(
                "io.axoniq.labs.chat.query.rooms.participants.RoomParticipant.findByParticipantAndRoomId")
                .setParameter("participant", participant)
                .setParameter("roomId", roomId)
        );
        System.out.println("## PARTICIPANT: " + roomParticipant);
        if (roomParticipant != null) {
            currentSession().remove(roomParticipant);
        }
    }

    RoomParticipant save(RoomParticipant roomParticipant) {
        return persist(roomParticipant);
    }

    void deleteAll() {
        currentSession().createQuery("DELETE FROM RoomParticipant").executeUpdate();
    }
}
