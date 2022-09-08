package app.family.domain.models.status

data class Status(
    val locationStatus: LocationStatus?,
    val weatherStatus: WeatherStatus?,
    val phoneStatus: PhoneStatus,
    val time: Long
)