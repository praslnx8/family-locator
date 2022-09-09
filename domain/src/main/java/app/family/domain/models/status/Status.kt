package app.family.domain.models.status

data class Status(
    val locationStatus: LocationStatus?,
    val weatherStatus: WeatherStatus?,
    val deviceStatus: DeviceStatus,
    val activityStatus: ActivityStatus?,
    val time: Long
)