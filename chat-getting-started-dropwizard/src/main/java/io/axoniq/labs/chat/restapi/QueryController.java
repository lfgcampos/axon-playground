package io.axoniq.labs.chat.restapi;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery;
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery;
import io.axoniq.labs.chat.query.rooms.messages.ChatMessage;
import io.axoniq.labs.chat.query.rooms.summary.RoomSummary;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.glassfish.jersey.server.ManagedAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import static org.axonframework.messaging.responsetypes.ResponseTypes.instanceOf;
import static org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf;

@Path("/rooms")
public class QueryController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QueryGateway gateway;

    public QueryController(QueryGateway gateway) {
        this.gateway = gateway;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void listRooms(@Suspended final AsyncResponse asyncResponse) {
        gateway.query(new AllRoomsQuery(),
                      multipleInstancesOf(RoomSummary.class))
               .thenAccept(response -> asyncResponse.resume(response));
    }

    @GET
    @Path("/{roomId}/participants")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void participantsInRoom(@Suspended final AsyncResponse asyncResponse,
                                   @PathParam("roomId") String roomId) {
        gateway.query(new RoomParticipantsQuery(roomId), multipleInstancesOf(String.class))
               .thenAccept(response -> asyncResponse.resume(response));
    }

    @GET
    @Path("/{roomId}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void roomMessages(@Suspended final AsyncResponse asyncResponse,
                             @PathParam("roomId") String roomId) {
        gateway.query(new RoomMessagesQuery(roomId), multipleInstancesOf(ChatMessage.class))
               .thenAccept(response -> asyncResponse.resume(response));
    }

    @GET
    @Path(value = "/{roomId}/messages/subscribe")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void subscribeRoomMessages(@Context Sse sse,
                                      @Context SseEventSink sseEventSink,
                                      @PathParam("roomId") String roomId) {
        Flux<ChatMessage> messages = queryMessages(roomId);
        messages.subscribe(msg -> sseEventSink.send(sse.newEventBuilder()
                                                       .name("message")
                                                       .data(msg.toString())
                                                       .build()));
    }

    private Flux<ChatMessage> queryMessages(String roomId) {
        SubscriptionQueryResult<List<ChatMessage>, ChatMessage> result
                = gateway.subscriptionQuery(new RoomMessagesQuery(roomId),
                                            multipleInstancesOf(ChatMessage.class),
                                            instanceOf(ChatMessage.class));
        Flux<ChatMessage> initialResult = result.initialResult().flatMapMany(Flux::fromIterable);
        return Flux.concat(initialResult, result.updates());
    }
}
