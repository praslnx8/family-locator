package app.family.domain.usecases

import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.UserApi
import app.family.domain.exceptions.NotLoggedInException
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FamilyInviteUseCase(
    private val userApi: UserApi,
    private val inviteApi: InviteApi,
    private val familyApi: FamilyApi,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun inviteFamily(): Flow<String> = flow {
        val randomInviteKey = randomPassCodeGenerator.generate(6).uppercase()
        val userId = userApi.getUser().first()?.id ?: throw NotLoggedInException()
        val familyId = getOrCreateFamilyId(userId).first()
        val familyPassword =
            familyApi.getFamilyPassword(userId).first() ?: throw NotLoggedInException()

        inviteApi.createInvite(randomInviteKey, familyId, familyPassword).collect()
        emit(randomInviteKey)
    }

    fun joinFamily(inviteKey: String): Flow<Boolean> = flow {
        val userDto = userApi.getUser().first() ?: throw NotLoggedInException()
        val invitationDto = inviteApi.getInviteDetails(inviteKey).first()
        if (invitationDto == null) {
            emit(false)
        } else {
            familyApi.joinFamily(userDto.id, invitationDto.familyId, invitationDto.familyPassword).collect()
        }
    }

    private suspend fun getOrCreateFamilyId(userId: String): Flow<String> = flow {
        val familyId = familyApi.getFamilyId(userId).first()
        if (familyId == null) {
            val passCode = randomPassCodeGenerator.generate()
            familyApi.createFamily(familyId = userId, password = passCode).collect()
            familyApi.joinFamily(userId = userId, familyId = userId, password = passCode).collect()
            emit(userId)
        } else {
            emit(familyId)
        }
    }
}