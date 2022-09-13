package app.family.api.apis

import androidx.datastore.core.DataStore
import app.family.api.models.StatusDto
import app.family.api.models.StatusProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MyStatusApi(
    private val statusDataStore: DataStore<StatusProto>
) {

    fun getStatus(): Flow<StatusDto> {
        return statusDataStore.data.map { convertToStatusDto(it) }
    }

    fun updateLocation(lat: Double, lon: Double, locality: String, time: Long): Flow<Unit> = flow {
        statusDataStore.updateData { oldData ->
            oldData.toBuilder()
                .setLat(lat)
                .setLon(lon)
                .setLocality(locality)
                .setLocationTime(time)
                .setUpdateTime(time)
                .build()
        }
        emit(Unit)
    }

    fun updateWeather(temperature: Double, weatherType: Int, time: Long): Flow<Unit> = flow {
        statusDataStore.updateData { oldData ->
            oldData.toBuilder()
                .setTemperature(temperature)
                .setWeatherType(weatherType)
                .setWeatherTime(time)
                .setUpdateTime(time)
                .build()
        }
        emit(Unit)
    }

    fun updatePhoneInfo(batteryPercentage: Int, isPhoneSilent: Boolean, time: Long): Flow<Unit> =
        flow {
            statusDataStore.updateData { oldData ->
                oldData.toBuilder()
                    .setBatteryPercentage(batteryPercentage)
                    .setIsPhoneSilent(isPhoneSilent)
                    .setUpdateTime(time)
                    .build()
            }
            emit(Unit)
        }

    fun updateActivity(activity: String, time: Long): Flow<Unit> = flow {
        statusDataStore.updateData { oldData ->
            oldData.toBuilder()
                .setActivity(activity)
                .setActivityTime(time)
                .setUpdateTime(time)
                .build()
        }
        emit(Unit)
    }

    private fun convertToStatusDto(statusProto: StatusProto): StatusDto {
        return StatusDto(
            lat = if (statusProto.lat != 0.0) statusProto.lat else null,
            lon = if (statusProto.lon != 0.0) statusProto.lon else null,
            locality = statusProto.locality.ifBlank { null },
            locationTime = if (statusProto.locationTime != 0L) statusProto.locationTime else null,
            temperature = if (statusProto.temperature != 0.0) statusProto.temperature else null,
            weatherType = statusProto.weatherType,
            weatherTime = if (statusProto.weatherTime != 0L) statusProto.weatherTime else null,
            activityType = statusProto.activity.ifBlank { null },
            isOnline = statusProto.isOnline,
            activityTime = if (statusProto.activityTime != 0L) statusProto.activityTime else null,
            isDeviceSilent = statusProto.isPhoneSilent,
            batteryPercentage = statusProto.batteryPercentage,
            updateTime = statusProto.updateTime,
        )
    }
}