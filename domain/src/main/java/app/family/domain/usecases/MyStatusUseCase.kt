package app.family.domain.usecases

import app.family.api.apis.MyStatusApi
import app.family.api.models.StatusDto
import app.family.domain.models.status.LocationStatus
import app.family.domain.models.status.PhoneStatus
import app.family.domain.models.status.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyStatusUseCase(
    private val myStatusApi: MyStatusApi
) {

    fun getStatus(): Flow<Status> {
        return myStatusApi.getStatus().map { statusDto ->
            Status(
                locationStatus = getLocationStatus(statusDto),
                phoneStatus = getPhoneStatus(statusDto),
                weatherStatus = null,
                time = statusDto.updateTime
            )
        }
    }

    private fun getLocationStatus(statusDto: StatusDto): LocationStatus? {
        if (statusDto.locality.isNotBlank()) {
            return LocationStatus(
                locality = statusDto.locality,
                lat = statusDto.lat,
                lon = statusDto.lon,
                time = statusDto.locationTime
            )
        }
        return null
    }

    private fun getPhoneStatus(statusDto: StatusDto): PhoneStatus {
        return PhoneStatus(
            isPhoneSilent = statusDto.isPhoneSilent,
            batteryPercentage = statusDto.batteryPercentage,
            time = statusDto.updateTime
        )
    }
}