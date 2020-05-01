package io.axoniq.labs.chat.query.rooms.messages;


import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ChatMessageDAO extends AbstractDAO<ChatMessage> {

    public ChatMessageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    ChatMessage save(ChatMessage chatMessage) {
        return persist(chatMessage);
    }

    List<ChatMessage> findAllByRoomIdOrderByTimestamp(String roomId) {
        return list((Query<ChatMessage>)
                            namedQuery(
                                    "io.axoniq.labs.chat.query.rooms.messages.ChatMessage.findAllByRoomIdOrderByTimestamp")
                                    .setParameter("roomId", roomId)
        );
    }

    void deleteAll() {
        currentSession().createQuery("DELETE FROM ChatMessage").executeUpdate();
    }
}
