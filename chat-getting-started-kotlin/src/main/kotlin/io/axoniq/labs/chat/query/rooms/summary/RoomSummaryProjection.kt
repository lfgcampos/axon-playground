package io.axoniq.labs.chat.query.rooms.summary

import io.axoniq.labs.chat.coreapi.AllRoomsQuery
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class RoomSummaryProjection(private val roomSummaryRepository: RoomSummaryRepository) {

    @QueryHandler
    fun handle(query: AllRoomsQuery): List<RoomSummary> = roomSummaryRepository.findAll()

    @EventHandler
    fun on(event: RoomCreatedEvent) {
        roomSummaryRepository.save(RoomSummary(roomId = event.roomId, name = event.name))
    }

    @EventHandler
    fun on(event: ParticipantJoinedRoomEvent) {
        roomSummaryRepository.getOne(event.roomId).addParticipant()
    }

    @EventHandler
    fun on(event: ParticipantLeftRoomEvent) {
        roomSummaryRepository.getOne(event.roomId).removeParticipant()
    }

}
