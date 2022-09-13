package app.family.domain.mappers

import app.family.api.models.MessageDto
import app.family.domain.models.Message

class MessageMapper {

    fun mapFromMessageDto(messageDto: MessageDto): Message {
        return Message(
            senderName = messageDto.senderName,
            message = messageDto.message,
            time = messageDto.time
        )
    }
}