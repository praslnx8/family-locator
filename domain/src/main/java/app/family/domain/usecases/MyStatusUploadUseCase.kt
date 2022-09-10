package app.family.domain.usecases

import app.family.api.apis.FamilyApi
import app.family.api.apis.FamilyStatusApi
import app.family.api.apis.MyStatusApi
import app.family.api.apis.UserApi
import app.family.domain.exceptions.NotLoggedInException
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class MyStatusUploadUseCase(
    private val userApi: UserApi,
    private val statusApi: MyStatusApi,
    private val familyApi: FamilyApi,
    private val familyStatusApi: FamilyStatusApi,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun uploadMyStatus(): Flow<Boolean> = flow {
        val user = userApi.getUser().first() ?: throw NotLoggedInException()
        statusApi.getStatus().collect { status ->
            val familyId = getOrCreateFamilyId(user.id).first()
            if (familyId != null) {
                val status =
                    familyStatusApi.pushStatus(user.id, user.name ?: "", familyId, status).first()
                emit(status)
            } else {
                emit(false)
            }
        }
    }

    private suspend fun getOrCreateFamilyId(userId: String): Flow<String?> = flow {
        val familyId = familyApi.getFamilyId(userId).first()
        if (familyId == null) {
            val passCode = randomPassCodeGenerator.generate()
            val isFamilyCreated =
                familyApi.createFamily(familyId = userId, password = passCode).first()
            if (isFamilyCreated) {
                familyApi.joinFamily(userId = userId, familyId = userId, password = passCode)
                    .collect()
                emit(userId)
            } else {
                emit(null)
            }
        } else {
            emit(familyId)
        }
    }
}