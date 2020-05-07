package io.axoniq.labs.chat.query.rooms.messages

import io.axoniq.labs.chat.coreapi.MessagePostedEvent
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ChatMessageProjection(
        private val repository: ChatMessageRepository,
        private val updateEmitter: QueryUpdateEmitter
) {

    @QueryHandler
    fun handle(query: RoomMessagesQuery): List<ChatMessage> = repository.findAllByRoomIdOrderByTimestamp(query.roomId)

    @EventHandler
    fun on(event: MessagePostedEvent, @Timestamp timestamp: Instant) {
        val chatMessage = ChatMessage(participant = event.participant, roomId = event.roomId, message = event.message, timestamp = timestamp.toEpochMilli())
        repository.save(chatMessage)
        updateEmitter.emit(
                RoomMessagesQuery::class.java,
                { it.roomId == event.roomId },
                chatMessage
        )
    }

}
