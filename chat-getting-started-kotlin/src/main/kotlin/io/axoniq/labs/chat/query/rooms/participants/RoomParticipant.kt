package io.axoniq.labs.chat.query.rooms.participants

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["roomId", "participant"])])
class RoomParticipant(

        @Id
        @GeneratedValue
        val id: Long? = null,
        var roomId: String? = null,
        var participant: String? = null
)

