package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.models.StatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MyStatusViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val myStatusUseCase: MyStatusUseCase,
) : ViewModel() {

    fun getMyStatus(): Flow<StatusState> {
        return combine(userUseCase.getUser(), myStatusUseCase.getStatus()) { user, status ->
            StatusState(
                name = user.name ?: "",
                activityType = status.activityStatus?.type,
                activityTime = status.activityStatus?.time,
                locality = status.locationStatus?.locality,
                locationTime = status.locationStatus?.time,
                isPhoneSilent = status.deviceStatus.isPhoneSilent,
                batteryPercentage = status.deviceStatus.batteryPercentage,
                weatherType = status.weatherStatus?.weatherType,
                weatherTime = status.weatherStatus?.time,
                time = status.time
            )
        }
    }
}