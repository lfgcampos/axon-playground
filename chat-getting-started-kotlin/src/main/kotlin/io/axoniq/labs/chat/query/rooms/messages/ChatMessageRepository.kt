package io.axoniq.labs.chat.query.rooms.messages

import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findAllByRoomIdOrderByTimestamp(roomId: String): List<ChatMessage>
}
