package app.family.api.apis

import android.util.Log
import app.family.api.BuildConfig
import app.family.api.models.WeatherDto
import app.family.api.network.WeatherApiClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherApi(private val weatherApiClient: WeatherApiClient) {

    fun fetchWeather(lat: Double, lon: Double): Flow<WeatherDto?> = callbackFlow {
        weatherApiClient.getWeather(lat, lon, BuildConfig.WEATHER_TOKEN)
            .enqueue(object : Callback<WeatherDto> {
                override fun onResponse(call: Call<WeatherDto>, response: Response<WeatherDto>) {
                    Log.i("Weather API", "Success Fetching weather")
                    trySend(response.body())
                }

                override fun onFailure(call: Call<WeatherDto>, t: Throwable) {
                    Log.e("Weather API", "Error Fetching weather " + t.message)
                    trySend(null)
                }
            })

        awaitClose()
    }
}