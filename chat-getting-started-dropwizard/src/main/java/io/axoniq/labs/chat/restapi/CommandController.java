package io.axoniq.labs.chat.restapi;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.JoinRoomCommand;
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand;
import io.axoniq.labs.chat.coreapi.PostMessageCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;

import java.util.UUID;
import java.util.concurrent.Future;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rooms")
public class CommandController {

    private final CommandGateway commandGateway;

    public CommandController(@SuppressWarnings("SpringJavaAutowiringInspection") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<String> createChatRoom(@Valid Room room) {
        return commandGateway.send(new CreateRoomCommand(UUID.randomUUID().toString(), room.getName()));
    }

    @POST
    @Path("/{roomId}/participants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Future<Void> joinChatRoom(@PathParam("roomId") String roomId, @Valid Participant participant) {
        return commandGateway.send(new JoinRoomCommand(roomId, participant.getName()));
    }

    @POST
    @Path("/{roomId}/messages")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Future<Void> postMessage(@PathParam("roomId") String roomId,
                                    @Valid PostMessageRequest message) {
        return commandGateway.send(new PostMessageCommand(roomId, message.getParticipant(), message.getMessage()));
    }

    @DELETE
    @Path("/{roomId}/participants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Future<Void> leaveChatRoom(@PathParam("roomId") String roomId, @Valid Participant participant) {
        return commandGateway.send(new LeaveRoomCommand(roomId, participant.getName()));
    }

    public static class PostMessageRequest {

        @NotEmpty
        private String participant;
        @NotEmpty
        private String message;

        public String getParticipant() {
            return participant;
        }

        public void setParticipant(String participant) {
            this.participant = participant;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Participant {

        @NotEmpty
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Room {

        private String roomId;
        @NotEmpty
        private String name;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
