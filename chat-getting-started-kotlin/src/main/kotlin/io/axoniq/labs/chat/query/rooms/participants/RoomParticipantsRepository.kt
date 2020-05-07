package io.axoniq.labs.chat.query.rooms.participants

import org.springframework.data.jpa.repository.JpaRepository

interface RoomParticipantsRepository : JpaRepository<RoomParticipant, Long> {
    fun findRoomParticipantsByRoomId(roomId: String): List<RoomParticipant>
    fun deleteByParticipantAndRoomId(participant: String, roomId: String)
}
