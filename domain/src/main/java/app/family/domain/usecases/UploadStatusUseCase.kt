package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.apis.FamilyApi
import app.family.api.apis.MyStatusApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

@OptIn(FlowPreview::class)
class UploadStatusUseCase(
    private val authApi: AuthApi,
    private val statusApi: MyStatusApi,
    private val familyApi: FamilyApi,
    private val familyCreateUseCase: FamilyCreateUseCase,
) {
    fun uploadMyStatus(): Flow<Unit> {
        return authApi.getUser().flatMapMerge { userDto ->
            familyCreateUseCase.getOrCreateFamilyId().flatMapMerge { familyId ->
                statusApi.getStatus().flatMapMerge { status ->
                    familyApi.pushStatusToFamily(
                        familyId = familyId,
                        userId = userDto.id,
                        name = userDto.name ?: "",
                        statusDto = status
                    )
                }
            }
        }
    }
}