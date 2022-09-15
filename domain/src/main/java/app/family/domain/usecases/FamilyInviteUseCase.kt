package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.InviteApi
import app.family.api.apis.UserApi
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class FamilyInviteUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val inviteApi: InviteApi,
    private val familyCreateUseCase: FamilyCreateUseCase,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun inviteFamily(): Flow<String> {
        val randomInviteKey = randomPassCodeGenerator.generate(6).uppercase()
        return authApi.getUser().flatMapMerge { userDto ->
            familyCreateUseCase.getOrCreateFamilyId().flatMapMerge { familyId ->
                userApi.getFamilyPassword(userDto.id).flatMapMerge { password ->
                    inviteApi.createInvite(randomInviteKey, familyId, password).map {
                        randomInviteKey
                    }
                }
            }
        }
    }

    fun joinFamily(inviteKey: String): Flow<Unit> {
        return authApi.getUser().flatMapMerge { userDto ->
            inviteApi.getInviteDetails(inviteKey).flatMapMerge { invitationDto ->
                userApi.joinFamily(
                    userId = userDto.id,
                    familyId = invitationDto.familyId,
                    password = invitationDto.familyPassword
                )
            }
        }
    }
}