package app.family.api.network

import app.family.api.models.WeatherDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiClient {

    @GET("weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<WeatherDto>
}