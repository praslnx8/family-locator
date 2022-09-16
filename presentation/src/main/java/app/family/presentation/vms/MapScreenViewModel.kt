package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.FetchFamilyStatusUseCase
import app.family.domain.usecases.MyStatusUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.models.MapDataState
import app.family.presentation.models.PositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val myStatusUseCase: MyStatusUseCase,
    private val familyStatusUseCase: FetchFamilyStatusUseCase
) : ViewModel() {

    fun getMapData(): Flow<List<MapDataState>> {
        return combine(
            userUseCase.getUser(),
            myStatusUseCase.getStatus(),
            familyStatusUseCase.fetchFamilyStatuses()
        ) { user, myStatus, familyStatus ->
            val mapDataStateList = mutableListOf<MapDataState>()
            myStatus.locationStatus?.also {
                mapDataStateList.add(MapDataState(user.name ?: "", PositionState(it.lat, it.lon)))
            }
            familyStatus.forEach { userStatus ->
                userStatus.status.locationStatus?.also {
                    mapDataStateList.add(
                        MapDataState(
                            userStatus.name,
                            PositionState(it.lat, it.lon)
                        )
                    )
                }
            }
            mapDataStateList
        }
    }
}