package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.FetchFamilyStatusUseCase
import app.family.presentation.models.StatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FamilyStatusViewModel @Inject constructor(
    private val fetchFamilyStatusUseCase: FetchFamilyStatusUseCase
) : ViewModel() {

    fun getFamilyStatuses(): Flow<List<StatusState>> {
        return fetchFamilyStatusUseCase.fetchFamilyStatuses().map {
            it.map { userStatus ->
                StatusState(
                    name = userStatus.name,
                    activityType = userStatus.status.activityStatus?.type,
                    activityTime = userStatus.status.activityStatus?.time,
                    locality = userStatus.status.locationStatus?.locality,
                    locationTime = userStatus.status.locationStatus?.time,
                    isPhoneSilent = userStatus.status.deviceStatus.isPhoneSilent,
                    batteryPercentage = userStatus.status.deviceStatus.batteryPercentage,
                    time = userStatus.status.time
                )
            }
        }
    }
}