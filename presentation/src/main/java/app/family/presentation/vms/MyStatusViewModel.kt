package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.models.MyStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MyStatusViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val myStatusUseCase: MyStatusUseCase,
) : ViewModel() {

    fun getMyStatus(): Flow<MyStatusState> {
        return combine(userUseCase.getUser(), myStatusUseCase.getStatus()) { user, status ->
            MyStatusState(
                name = user.name,
                activityType = status.activityStatus?.type,
                activityTime = status.activityStatus?.time,
                locality = status.locationStatus?.locality,
                locationTime = status.locationStatus?.time,
                isPhoneSilent = status.deviceStatus.isPhoneSilent,
                batteryPercentage = status.deviceStatus.batteryPercentage,
                time = status.time
            )
        }
    }
}