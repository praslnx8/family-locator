package app.family.domain.models

data class Message(
    val senderName: String,
    val message: String,
    val time: Long
)