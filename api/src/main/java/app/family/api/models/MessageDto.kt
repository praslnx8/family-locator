package app.family.api.models

import com.google.firebase.database.PropertyName

data class MessageDto(
    @get:PropertyName("sender_name")
    @set:PropertyName("sender_name")
    var senderName: String = "",

    @get:PropertyName("message")
    @set:PropertyName("message")
    var message: String = "",

    @get:PropertyName("time")
    @set:PropertyName("time")
    var time: Long = 0L
)