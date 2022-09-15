package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.MessageApi
import app.family.api.apis.UserApi
import app.family.domain.mappers.MessageMapper
import app.family.domain.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MessageUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val messageApi: MessageApi,
    private val messageMapper: MessageMapper
) {

    fun sendMessage(message: String): Flow<Boolean> = flow {
        val user = authApi.getUser().first()
        val familyId = userApi.getFamilyId(user?.id ?: "").first() ?: ""
        val messageSent = messageApi.sendMessage(
            familyId = familyId,
            senderId = user?.id ?: "",
            senderName = user?.name ?: "",
            message = message,
            time = System.currentTimeMillis()
        ).first()
        emit(messageSent)
    }

    fun listenToChat(): Flow<List<Message>> = flow {
        messageApi.fetchMessages().collect { messageList ->
            emit(messageList.map { messageDto ->
                messageMapper.mapFromMessageDto(messageDto)
            })
        }
    }
}