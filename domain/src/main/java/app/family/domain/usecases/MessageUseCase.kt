package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.MessageApi
import app.family.domain.mappers.MessageMapper
import app.family.domain.models.Message
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class MessageUseCase(
    private val authApi: AuthApi,
    private val familyCreateUseCase: FamilyCreateUseCase,
    private val messageApi: MessageApi,
    private val messageMapper: MessageMapper
) {

    fun sendMessage(message: String): Flow<Unit> {
        return authApi.getUser().flatMapMerge { userDto ->
            familyCreateUseCase.getOrCreateFamilyId().flatMapMerge { familyId ->
                messageApi.sendMessage(
                    familyId = familyId,
                    senderId = userDto.id,
                    senderName = userDto.name ?: "",
                    message = message,
                    time = System.currentTimeMillis()
                )
            }
        }
    }

    fun getUnReadMessages(): Flow<List<Message>> {
        return messageApi.getLastSyncedTime().flatMapMerge { lastSyncedTime ->
            messageApi.fetchMessages(lastSyncedTime).map { messageDtoList ->
                messageDtoList.map { messageDto ->
                    messageMapper.mapFromMessageDto(messageDto)
                }
            }
        }
    }

    fun listenToChat(): Flow<List<Message>> {
        return messageApi.fetchMessages().map { messageList ->
            messageList.map { messageDto -> messageMapper.mapFromMessageDto(messageDto) }
        }
    }

    fun setLastSyncTime(): Flow<Unit> {
        return messageApi.setLastSyncedTime(System.currentTimeMillis())
    }
}