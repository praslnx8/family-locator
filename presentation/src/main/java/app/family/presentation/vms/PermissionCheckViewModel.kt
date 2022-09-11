package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.presentation.models.PermissionCheckState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionCheckViewModel: ViewModel() {

    private val _permissionCheckState = MutableStateFlow(PermissionCheckState())
    val permissionCheckState: StateFlow<PermissionCheckState> = _permissionCheckState

    fun setSoftPermissionGranted(isGranted: Boolean) {
        _permissionCheckState.value =
            permissionCheckState.value.copy(softAskPermissionsGranted = isGranted)
    }

    fun setHardPermissionGranted(isGranted: Boolean) {
        _permissionCheckState.value =
            permissionCheckState.value.copy(hardAskPermissionGranted = isGranted)
    }
}