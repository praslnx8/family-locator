package app.family.api.models

data class WeatherDto(
    val weather: List<Weather>,
    val main: Main
) {

    fun getTemp(): Double {
        return main.temp
    }

    fun getWeather(): String {
        return weather.firstOrNull()?.main ?: ""
    }

    data class Main(
        val temp: Double
    )

    data class Weather(
        val main: String
    )
}
