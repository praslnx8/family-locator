package app.family.domain.usecases

import app.family.api.apis.MessageApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

@OptIn(FlowPreview::class)
class UpdateMessageUseCase(
    private val familyCreateUseCase: FamilyCreateUseCase,
    private val messageApi: MessageApi,
) {
    fun syncAndUpdateMessages(): Flow<Unit> {
        return familyCreateUseCase.getOrCreateFamilyId().flatMapMerge { familyId ->
            messageApi.listenToMessage(familyId).flatMapMerge { messages ->
                messageApi.storeMessages(messages)
            }
        }
    }

}