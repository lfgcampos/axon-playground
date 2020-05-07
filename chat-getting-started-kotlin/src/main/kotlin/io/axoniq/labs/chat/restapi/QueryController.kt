package io.axoniq.labs.chat.restapi

import io.axoniq.labs.chat.coreapi.AllRoomsQuery
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery
import io.axoniq.labs.chat.query.rooms.messages.ChatMessage
import io.axoniq.labs.chat.query.rooms.summary.RoomSummary
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.concurrent.Future

@RestController
class QueryController(private val gateway: QueryGateway) {

    @GetMapping("rooms")
    fun listRooms(): Future<List<RoomSummary>> = gateway.query(AllRoomsQuery(), MultipleInstancesResponseType(RoomSummary::class.java))

    @GetMapping("/rooms/{roomId}/participants")
    fun participantsInRoom(@PathVariable roomId: String): Future<List<String>> =
            gateway.query(RoomParticipantsQuery(roomId), MultipleInstancesResponseType(String::class.java))

    @GetMapping("/rooms/{roomId}/messages")
    fun roomMessages(@PathVariable roomId: String): Future<List<ChatMessage>> =
            gateway.query(RoomMessagesQuery(roomId), MultipleInstancesResponseType(ChatMessage::class.java))

    @GetMapping(value = ["/rooms/{roomId}/messages/subscribe"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribeRoomMessages(@PathVariable roomId: String): Flux<ChatMessage> {
        val query = RoomMessagesQuery(roomId)
        val result = gateway.subscriptionQuery(
                query,
                ResponseTypes.multipleInstancesOf(ChatMessage::class.java),
                ResponseTypes.instanceOf(ChatMessage::class.java)
        )
        val initialResult = result.initialResult().flatMapMany { Flux.fromIterable(it) }
        return Flux.concat(initialResult, result.updates())
    }

}
