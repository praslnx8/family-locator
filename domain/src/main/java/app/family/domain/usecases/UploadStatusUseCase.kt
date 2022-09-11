package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.MyStatusApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UploadStatusUseCase(
    private val authApi: AuthApi,
    private val statusApi: MyStatusApi,
    private val familyApi: FamilyApi,
    private val familyCreateUseCase: FamilyCreateUseCase,
) {
    fun uploadMyStatus(): Flow<Boolean> = flow {
        val user = authApi.getUser().first()
        if (user == null) {
            emit(false)
            return@flow
        }
        statusApi.getStatus().collect { status ->
            val familyId = familyCreateUseCase.getOrCreateFamilyId().first()
            if (familyId != null) {
                val isStatusUploaded =
                    familyApi.pushStatusToFamily(
                        familyId = familyId,
                        userId = user.id,
                        name = user.name ?: "",
                        statusDto = status
                    ).first()
                emit(isStatusUploaded)
            } else {
                emit(false)
            }
        }
    }
}