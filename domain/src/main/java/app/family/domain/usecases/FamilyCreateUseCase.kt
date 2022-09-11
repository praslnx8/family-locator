package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.UserApi
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FamilyCreateUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val familyApi: FamilyApi,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun getOrCreateFamilyId(): Flow<String?> = flow {
        val userId = authApi.getUser().first()?.id ?: ""
        val familyId = userApi.getFamilyId(userId).first()
        if (familyId == null) {
            val passCode = randomPassCodeGenerator.generate()
            val isFamilyCreated =
                familyApi.createFamily(familyId = userId, password = passCode).first()
            if (isFamilyCreated) {
                userApi.joinFamily(userId = userId, familyId = userId, password = passCode)
                    .first()
                emit(userId)
            } else {
                emit(null)
            }
        } else {
            emit(familyId)
        }
    }
}