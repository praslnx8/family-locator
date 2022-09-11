package app.family.presentation.models

data class PermissionCheckState(
    val softAskPermissionsGranted: Boolean = false,
    val hardAskPermissionGranted: Boolean = false
)