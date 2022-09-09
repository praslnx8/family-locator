package app.family.domain.models.status

data class DeviceStatus(
    val isPhoneSilent: Boolean,
    val batteryPercentage: Int,
    val time: Long
)
