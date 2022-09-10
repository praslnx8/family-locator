package app.family.domain.usecases

import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.UserApi
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FamilyInviteUseCase(
    private val userApi: UserApi,
    private val inviteApi: InviteApi,
    private val familyApi: FamilyApi,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun inviteFamily(): Flow<String?> = flow {
        val randomInviteKey = randomPassCodeGenerator.generate(6).uppercase()
        val userId = userApi.getUser().first()?.id
        if (userId == null) {
            emit(null)
        } else {
            val familyId = getOrCreateFamilyId(userId).first()
            if (familyId != null) {
                val familyPassword =
                    familyApi.getFamilyPassword(userId).first()
                if (familyPassword != null) {
                    val isInviteCreated =
                        inviteApi.createInvite(randomInviteKey, familyId, familyPassword).first()
                    if (isInviteCreated) {
                        emit(randomInviteKey)
                    } else {
                        emit(null)
                    }
                } else {
                    emit(null)
                }
            } else {
                emit(null)
            }
        }
    }

    fun joinFamily(inviteKey: String): Flow<Boolean> = flow {
        val userDto = userApi.getUser().first()
        if (userDto == null) {
            emit(false)
        } else {
            val invitationDto = inviteApi.getInviteDetails(inviteKey).first()
            if (invitationDto == null) {
                emit(false)
            } else {
                val isFamilyJoined = familyApi.joinFamily(
                    userDto.id,
                    invitationDto.familyId,
                    invitationDto.familyPassword
                ).first()
                emit(isFamilyJoined)
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