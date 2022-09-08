package app.family.domain.models.status

data class WeatherStatus(
    val weatherType: WeatherType,
    val temperature: Double
)