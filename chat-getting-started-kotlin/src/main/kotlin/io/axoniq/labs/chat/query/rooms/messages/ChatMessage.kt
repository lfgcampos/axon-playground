package io.axoniq.labs.chat.query.rooms.messages

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class ChatMessage(

        @Id
        @GeneratedValue
        val id: Long? = null,
        var timestamp: Long = 0,
        var roomId: String? = null,
        var message: String? = null,
        var participant: String? = null
)
