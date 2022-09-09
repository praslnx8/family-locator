package app.family.api.models

data class StatusDto(
    val lat: Double?,
    val lon: Double?,
    val locality: String?,
    val locationTime: Long?,

//Weather
    val temperature: Double?,
    val weatherType: String?,
    val weatherTime: Long?,

//Activity
    val activity: String?,
    val isOnline: Boolean?,
    val activityTime: Long?,

//Phone Status
    val isPhoneSilent: Boolean,
    val batteryPercentage: Int,

//Overall Data
    val updateTime: Long,
)