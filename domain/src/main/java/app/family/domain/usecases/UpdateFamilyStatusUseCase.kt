package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UpdateFamilyStatusUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val familyApi: FamilyApi,
) {

    fun listenAndUpdateFamilyStatus(): Flow<Unit> = flow {
        val userId = authApi.getUser().first()?.id ?: ""
        val familyId = userApi.getFamilyId(userId).first() ?: return@flow
        familyApi.listenToFamilyUpdatesAndPush(familyId).collect {
            emit(Unit)
        }
    }
}