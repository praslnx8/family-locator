package app.family.domain.usecases

import app.family.api.apis.DeviceApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import app.family.api.apis.WeatherApi
import app.family.api.models.LocationDto
import app.family.domain.models.status.ActivityType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MyStatusSyncUseCase(
    private val locationAPI: LocationAPI,
    private val localityApi: LocalityApi,
    private val statusApi: MyStatusApi,
    private val deviceApi: DeviceApi,
    private val weatherApi: WeatherApi
) {

    fun listenAndSync(): Flow<Unit> {
        return combine(listenAndSyncLocationApi(), syncDeviceApi()) { _, _ -> }
    }

    fun syncNow(): Flow<Unit> {
        return combine(syncLocationApi(), syncDeviceApi()) { _, _ -> }
    }

    fun syncActivityDetection(activityType: ActivityType): Flow<Unit> {
        return statusApi.updateActivity(activityType.name, System.currentTimeMillis())
    }

    private fun listenAndSyncLocationApi(): Flow<Unit> {
        return locationAPI.listenToLocationChange().map { locationDto ->
            updateLocation(locationDto)
            syncDeviceApi().collect()
            syncWeather(locationDto)
        }
    }

    private fun syncLocationApi(): Flow<Unit> {
        return locationAPI.fetchLocation().map { location ->
            if (location != null) {
                updateLocation(location)
                syncDeviceApi().collect()
                syncWeather(location)
            }
        }
    }

    private suspend fun updateLocation(locationDto: LocationDto) {
        val locality = localityApi.getLocality(locationDto.lat, locationDto.lon).first()
        if (locality != null) {
            statusApi.updateLocation(
                locationDto.lat,
                locationDto.lon,
                locality,
                System.currentTimeMillis()
            ).collect()
        }
    }

    private suspend fun syncWeather(locationDto: LocationDto) {
        val weatherDto = weatherApi.fetchWeather(locationDto.lat, locationDto.lon).first()
        if (weatherDto != null) {
            statusApi.updateWeather(
                weatherDto.getTemp(),
                weatherDto.getWeather(),
                System.currentTimeMillis()
            ).collect()
        }
    }

    private fun syncDeviceApi(): Flow<Unit> {
        val batteryPercentage = deviceApi.getBatteryPercentage()
        val isPhoneSilent = deviceApi.isPhoneSilent()
        return statusApi.updatePhoneInfo(
            batteryPercentage,
            isPhoneSilent,
            System.currentTimeMillis()
        )
    }
}