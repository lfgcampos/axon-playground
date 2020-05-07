package io.axoniq.labs.chat.commandmodel

import io.axoniq.labs.chat.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ChatRoom {

    @AggregateIdentifier
    private var roomId: String? = null
    private var participants: MutableSet<String> = mutableSetOf()

    constructor() {} // required by Axon

    @CommandHandler
    constructor(command: CreateRoomCommand) {
        AggregateLifecycle.apply(RoomCreatedEvent(command.roomId, command.name))
    }

    @CommandHandler
    fun handle(command: JoinRoomCommand) {
        if (!participants.contains(command.participant)) {
            AggregateLifecycle.apply(ParticipantJoinedRoomEvent(roomId!!, command.participant))
        }
    }

    @CommandHandler
    fun handle(command: LeaveRoomCommand) {
        if (participants.contains(command.participant)) {
            AggregateLifecycle.apply(ParticipantLeftRoomEvent(roomId!!, command.participant))
        }
    }

    @CommandHandler
    fun handle(command: PostMessageCommand) {
        check(participants.contains(command.participant)) { "You cannot post messages unless you've joined the chat room" }
        AggregateLifecycle.apply(
                MessagePostedEvent(roomId!!, command.participant, command.message)
        )
    }

    @EventSourcingHandler
    protected fun on(event: RoomCreatedEvent) {
        roomId = event.roomId
    }

    @EventSourcingHandler
    protected fun on(event: ParticipantJoinedRoomEvent) {
        participants.add(event.participant)
    }

    @EventSourcingHandler
    protected fun on(event: ParticipantLeftRoomEvent) {
        participants.remove(event.participant)
    }
}
