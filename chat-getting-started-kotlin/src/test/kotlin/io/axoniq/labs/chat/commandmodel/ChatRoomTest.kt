package io.axoniq.labs.chat.commandmodel

import io.axoniq.labs.chat.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChatRoomTest {
    private lateinit var testFixture: AggregateTestFixture<ChatRoom>

    @BeforeEach
    fun setUp() {
        testFixture = AggregateTestFixture(ChatRoom::class.java)
    }

    @Test
    fun testCreateChatRoom() {
        testFixture.givenNoPriorActivity()
            .`when`(CreateRoomCommand("roomId", "test-room"))
            .expectEvents(RoomCreatedEvent("roomId", "test-room"))
    }

    @Test
    fun testJoinChatRoom() {
        testFixture.given(RoomCreatedEvent("roomId", "test-room"))
            .`when`(JoinRoomCommand("roomId", "participant"))
            .expectEvents(ParticipantJoinedRoomEvent("roomId", "participant"))
    }

    @Test
    fun testPostMessage() {
        testFixture.given(
            RoomCreatedEvent("roomId", "test-room"),
            ParticipantJoinedRoomEvent("roomId", "participant")
        )
            .`when`(PostMessageCommand("roomId", "participant", "Hi there!"))
            .expectEvents(MessagePostedEvent("roomId", "participant", "Hi there!"))
    }

    @Test
    fun testCannotJoinChatRoomTwice() {
        testFixture.given(
            RoomCreatedEvent("roomId", "test-room"),
            ParticipantJoinedRoomEvent("roomId", "participant")
        )
            .`when`(JoinRoomCommand("roomId", "participant"))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun testCannotLeaveChatRoomTwice() {
        testFixture.given(
            RoomCreatedEvent("roomId", "test-room"),
            ParticipantJoinedRoomEvent("roomId", "participant"),
            ParticipantLeftRoomEvent("roomId", "participant")
        )
            .`when`(LeaveRoomCommand("roomId", "participant"))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun testParticipantCannotPostMessagesOnceHeLeftTheRoom() {
        testFixture.given(
            RoomCreatedEvent("roomId", "test-room"),
            ParticipantJoinedRoomEvent("roomId", "participant"),
            ParticipantLeftRoomEvent("roomId", "participant")
        )
            .`when`(PostMessageCommand("roomId", "participant", "Hi there!"))
            .expectException(IllegalStateException::class.java)
            .expectNoEvents()
    }
}
