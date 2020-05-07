package io.axoniq.labs.chat.query.rooms.summary

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class RoomSummary(

        @Id
        var roomId: String? = null,
        var name: String? = null,
        var participants: Int = 0
) {
    fun addParticipant() {
        participants++
    }

    fun removeParticipant() {
        participants--
    }
}
