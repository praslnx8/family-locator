package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.UserApi
import app.family.domain.utils.RandomPassCodeGenerator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class FamilyCreateUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val familyApi: FamilyApi,
    private val randomPassCodeGenerator: RandomPassCodeGenerator
) {

    fun getOrCreateFamilyId(): Flow<String> {
        val passCode = randomPassCodeGenerator.generate()

        return authApi.getUser()
            .flatMapMerge { userDto ->
                userApi.getFamilyId(userDto.id)
                    .flatMapMerge { familyId ->
                        if (familyId == null) {
                            familyApi.createFamily(familyId = userDto.id, password = passCode)
                                .flatMapMerge {
                                    userApi.joinFamily(
                                        userId = userDto.id,
                                        familyId = userDto.id,
                                        password = passCode
                                    ).map {
                                        userDto.id
                                    }
                                }
                        } else {
                            flowOf(familyId)
                        }
                    }
            }
    }
}