package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.InviteApi
import app.family.api.apis.UserApi
import app.family.api.models.UserDto
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FamilyInviteUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val inviteApi: InviteApi,
    private val familyCreateUseCase: FamilyCreateUseCase,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun inviteFamily(): Flow<String?> = flow {
        val randomInviteKey = randomPassCodeGenerator.generate(6).uppercase()
        val userId = authApi.getUser().first()?.id ?: ""
        val familyId = familyCreateUseCase.getOrCreateFamilyId().first()
        val familyPassword = userApi.getFamilyPassword(userId).first()
        if (familyId != null && familyPassword != null) {
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
    }

    fun joinFamily(inviteKey: String): Flow<Boolean> = flow {
        val userDto = authApi.getUser().first() ?: UserDto()
        val invitationDto = inviteApi.getInviteDetails(inviteKey).first()
        if (invitationDto == null) {
            emit(false)
        } else {
            val isFamilyJoined = userApi.joinFamily(
                userDto.id,
                invitationDto.familyId,
                invitationDto.familyPassword
            ).first()
            emit(isFamilyJoined)
        }
    }
}