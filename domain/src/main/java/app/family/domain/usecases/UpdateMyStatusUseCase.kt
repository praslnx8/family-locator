package app.family.domain.usecases

import app.family.api.apis.DeviceApi
import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UpdateMyStatusUseCase(
    private val locationAPI: LocationAPI,
    private val localityApi: LocalityApi,
    private val deviceApi: DeviceApi,
    private val statusApi: MyStatusApi
) {

    fun update(): Flow<Unit> = flow {

        //Update Location
        val location = locationAPI.fetchLocation().first()
        if (location != null) {
            val locality = localityApi.getLocality(location.lat, location.lon).first()
            if (locality != null) {
                statusApi.updateLocation(
                    location.lat,
                    location.lon,
                    locality,
                    System.currentTimeMillis()
                )
            }
        }

        //Update Activity

        //Update Device Settings
        val batteryPercentage = deviceApi.getBatteryPercentage()
        val isPhoneSilent = deviceApi.isPhoneSilent()
        statusApi.updatePhoneInfo(batteryPercentage, isPhoneSilent, System.currentTimeMillis())


        emit(Unit)
    }
}