package app.family.api.models

data class WeatherDto(
    val weather: List<Weather>,
    val main: Main
) {

    fun getTemp(): Double {
        return main.temp
    }

    fun getWeather(): Int {
        return weather.firstOrNull()?.id ?: 0
    }

    data class Main(
        val temp: Double
    )

    data class Weather(
        val id: Int
    )
}
