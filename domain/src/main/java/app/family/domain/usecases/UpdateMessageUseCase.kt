package app.family.domain.usecases

import android.util.Log
import app.family.api.apis.AuthApi
import app.family.api.apis.MessageApi
import app.family.api.apis.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UpdateMessageUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val messageApi: MessageApi,
) {
    fun syncAndUpdateMessages(): Flow<Unit> = flow {
        val user = authApi.getUser().first()
        val familyId = userApi.getFamilyId(user?.id ?: "").first() ?: ""
        messageApi.listenToMessage(familyId).collect() {
            Log.i("Update Message UseCase", "Messages Came")
            messageApi.storeMessages(it).first()
            Log.i("Update Message UseCase", "Stored messages")
            emit(Unit)
        }
    }

}