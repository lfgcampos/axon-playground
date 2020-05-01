package io.axoniq.labs.chat.query.rooms.summary;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RoomSummaryDAO extends AbstractDAO<RoomSummary> {

    public RoomSummaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    RoomSummary save(RoomSummary roomSummary) {
        return persist(roomSummary);
    }

    Optional<RoomSummary> findById(String roomId) {
        return Optional.ofNullable(get(roomId));
    }

    List<RoomSummary> findAll() {
        return list((Query<RoomSummary>) namedQuery("io.axoniq.labs.chat.query.rooms.summary.RoomSummary.findAll"));
    }

    void deleteAll() {
        currentSession().createQuery("DELETE FROM RoomSummary").executeUpdate();
    }
}
