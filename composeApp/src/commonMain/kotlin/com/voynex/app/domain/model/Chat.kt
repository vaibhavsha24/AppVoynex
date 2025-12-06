package com.voynex.app.domain.model
data class ChatMessage(
    val id: String,
    val text: String,
    val isMine: Boolean,
    val time: String = "",
)
