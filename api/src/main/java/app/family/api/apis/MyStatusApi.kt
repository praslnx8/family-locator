package app.family.api.apis

import androidx.datastore.core.DataStore
import app.family.api.mappers.StatusMapper
import app.family.api.models.StatusDto
import app.family.api.models.StatusProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MyStatusApi(
    private val statusDataStore: DataStore<StatusProto>,
    private val statusMapper: StatusMapper
) {

    fun getStatus(): Flow<StatusDto> {
        return statusDataStore.data.map { statusMapper.mapToStatusDto(it) }
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
}