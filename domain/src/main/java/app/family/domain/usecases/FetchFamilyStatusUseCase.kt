package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.domain.mappers.UserStatusMapper
import app.family.domain.models.status.UserStatus
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class FetchFamilyStatusUseCase(
    private val authApi: AuthApi,
    private val familyApi: FamilyApi,
    private val userStatusMapper: UserStatusMapper
) {
    fun fetchFamilyStatuses(): Flow<List<UserStatus>> {
        return authApi.getUser().flatMapMerge { userDto ->
            familyApi.fetchFamilyUpdates().map { userStatusMap ->
                userStatusMap.filterKeys { it != userDto.id }.values.toList().filterNotNull()
                    .map { userStatusDto ->
                        userStatusMapper.mapFromUserStatusDto(userStatusDto)
                    }
            }
        }
    }
}