package app.family.domain.usecases

import app.family.api.apis.DeviceApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
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
    private val deviceApi: DeviceApi
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
            val locality = localityApi.getLocality(locationDto.lat, locationDto.lon).first()
            if (locality != null) {
                statusApi.updateLocation(
                    locationDto.lat,
                    locationDto.lon,
                    locality,
                    System.currentTimeMillis()
                ).collect()
            }
            syncDeviceApi().collect()
        }
    }

    private fun syncLocationApi(): Flow<Unit> {
        return locationAPI.fetchLocation().map { location ->
            if (location != null) {
                val locality = localityApi.getLocality(location.lat, location.lon).first()
                if (locality != null) {
                    statusApi.updateLocation(
                        location.lat,
                        location.lon,
                        locality,
                        System.currentTimeMillis()
                    ).collect()
                }
                syncDeviceApi().collect()
            }
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