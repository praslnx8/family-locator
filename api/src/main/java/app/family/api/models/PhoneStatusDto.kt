package app.family.api.models

data class PhoneStatusDto(
    val batteryPercentage: Int,
    val isPhoneSilent: Boolean
)