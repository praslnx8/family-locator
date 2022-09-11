package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.UserApi
import app.family.domain.mappers.UserStatusMapper
import app.family.domain.models.status.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FetchFamilyStatusUseCase(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val familyApi: FamilyApi,
    private val userStatusMapper: UserStatusMapper
) {

    fun fetchFamilyStatuses(): Flow<List<UserStatus>> = flow {
        val userId = authApi.getUser().first()?.id ?: ""
        val familyId = userApi.getFamilyId(userId).first()
        if (familyId == null) {
            emit(emptyList())
            return@flow
        }
        familyApi.listenToFamilyUpdates(familyId).collect { userStatusMap ->
            emit(
                userStatusMap.filterKeys { it != userId }.values.toList().filterNotNull()
                    .map { userStatusDto ->
                        userStatusMapper.mapFromUserStatusDto(userStatusDto)
                    })
        }
    }
}