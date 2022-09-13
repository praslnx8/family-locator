package app.family.presentation.models

data class MessageViewState(
    val messages: List<MessageState> = emptyList()
)

data class MessageState(
    val name: String,
    val message: String,
    val time: Long
)