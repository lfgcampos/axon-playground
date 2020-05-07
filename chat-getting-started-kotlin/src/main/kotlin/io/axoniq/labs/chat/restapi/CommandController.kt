package io.axoniq.labs.chat.restapi

import io.axoniq.labs.chat.coreapi.CreateRoomCommand
import io.axoniq.labs.chat.coreapi.JoinRoomCommand
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand
import io.axoniq.labs.chat.coreapi.PostMessageCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.Future
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
class CommandController(private val commandGateway: CommandGateway) {

    @PostMapping("/rooms")
    fun createChatRoom(@RequestBody room: @Valid Room): Future<String?>? {
        requireNotNull(room.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(CreateRoomCommand(room.roomId ?: UUID.randomUUID().toString(), room.name))
    }

    @PostMapping("/rooms/{roomId}/participants")
    fun joinChatRoom(@PathVariable roomId: String, @RequestBody participant: @Valid Participant): Future<Void> {
        requireNotNull(participant.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(JoinRoomCommand(roomId, participant.name))
    }

    @PostMapping("/rooms/{roomId}/messages")
    fun postMessage(@PathVariable roomId: String, @RequestBody message: @Valid PostMessageRequest): Future<Void> {
        requireNotNull(message.participant) { "'name' missing - please provide a participant name" }
        requireNotNull(message.message) { "'message' missing - please provide a message to post" }
        return commandGateway.send(PostMessageCommand(roomId, message.participant, message.message))
    }

    @DeleteMapping("/rooms/{roomId}/participants")
    fun leaveChatRoom(@PathVariable roomId: String, @RequestBody participant: @Valid Participant): Future<Void?>? {
        requireNotNull(participant.name) { "name is mandatory for a chatroom" }
        return commandGateway.send(LeaveRoomCommand(roomId, participant.name))
    }

    data class PostMessageRequest(
            val participant: @NotEmpty String? = null,
            val message: @NotEmpty String? = null
    )

    class Participant(
            val name: @NotEmpty String? = null
    )

    class Room(
            val roomId: String? = null,
            val name: @NotEmpty String? = null
    )
}
