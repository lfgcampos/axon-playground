package io.axoniq.labs.chat.query.rooms.summary

import org.springframework.data.jpa.repository.JpaRepository

interface RoomSummaryRepository : JpaRepository<RoomSummary, String>
