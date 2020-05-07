package io.axoniq.labs.chat.query.rooms.participants

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class RoomParticipantsProjection(private val repository: RoomParticipantsRepository) {

    @QueryHandler
    fun handle(query: RoomParticipantsQuery): List<String> {
        return repository.findRoomParticipantsByRoomId(query.roomId)
                .mapNotNull(RoomParticipant::participant)
                .sorted()
    }

    @EventHandler
    fun on(event: ParticipantJoinedRoomEvent) {
        repository.save(RoomParticipant(roomId = event.roomId, participant = event.participant))
    }

    @EventHandler
    fun on(event: ParticipantLeftRoomEvent) {
        repository.deleteByParticipantAndRoomId(event.participant, event.roomId)
    }

}
