package app.family.domain.models

data class Message(
    val senderId: String,
    val senderName: String,
    val message: String,
    val time: Long
)