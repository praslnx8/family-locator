package app.family.domain.usecases

import app.family.api.apis.FamilyApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

@OptIn(FlowPreview::class)
class UpdateFamilyStatusUseCase(
    private val familyCreateUseCase: FamilyCreateUseCase,
    private val familyApi: FamilyApi,
) {

    fun listenAndUpdateFamilyStatus(): Flow<Unit> {
        return familyCreateUseCase.getOrCreateFamilyId().flatMapMerge { familyId ->
            familyApi.listenToFamilyUpdates(familyId).flatMapMerge { userStatusMap ->
                familyApi.storeFamilyUpdates(userStatusMap)
            }
        }
    }
}