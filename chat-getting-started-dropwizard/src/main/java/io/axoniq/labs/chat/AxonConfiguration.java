package io.axoniq.labs.chat;

import io.axoniq.labs.chat.commandmodel.ChatRoom;
import io.axoniq.labs.chat.query.rooms.messages.ChatMessageProjection;
import io.axoniq.labs.chat.query.rooms.participants.RoomParticipantsProjection;
import io.axoniq.labs.chat.query.rooms.summary.RoomSummaryProjection;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

public class AxonConfiguration {

    public Configuration buildAndGetConfiguration(ChatMessageProjection chatMessageProjection,
                                                  RoomParticipantsProjection roomParticipantsProjection,
                                                  RoomSummaryProjection roomSummaryProjection) {
        return DefaultConfigurer
                .defaultConfiguration() // defaultConfiguration already set a lot for us
                .configureAggregate(ChatRoom.class) // this is our Aggregate
                .eventProcessing(conf -> conf
                        .registerTokenStore(config -> new InMemoryTokenStore()))
                .registerQueryHandler(conf -> {
                    chatMessageProjection.setUpdateEmitter(conf.queryUpdateEmitter());
                    return chatMessageProjection;
                })
                .registerEventHandler(conf -> {
                    chatMessageProjection.setUpdateEmitter(conf.queryUpdateEmitter());
                    return chatMessageProjection;
                })
                .registerQueryHandler(conf -> roomParticipantsProjection)
                .registerEventHandler(conf -> roomParticipantsProjection)
                .registerQueryHandler(conf -> roomSummaryProjection)
                .registerEventHandler(conf -> roomSummaryProjection)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .buildConfiguration();
    }
}
