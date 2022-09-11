package app.family.domain.mappers

import app.family.api.models.StatusDto
import app.family.domain.models.status.ActivityStatus
import app.family.domain.models.status.ActivityType
import app.family.domain.models.status.DeviceStatus
import app.family.domain.models.status.LocationStatus
import app.family.domain.models.status.Status

class StatusMapper {

    fun mapFromStatusDto(statusDto: StatusDto): Status {
        return Status(
            locationStatus = getLocationStatus(statusDto),
            deviceStatus = getDeviceStatus(statusDto),
            weatherStatus = null,
            activityStatus = getActivityStatus(statusDto),
            time = statusDto.updateTime
        )
    }

    private fun getLocationStatus(statusDto: StatusDto): LocationStatus? {
        if (statusDto.locality.isNullOrBlank().not()) {
            return LocationStatus(
                locality = statusDto.locality?:"",
                lat = statusDto.lat?:0.0,
                lon = statusDto.lon?:0.0,
                time = statusDto.locationTime?:0L
            )
        }
        return null
    }

    private fun getDeviceStatus(statusDto: StatusDto): DeviceStatus {
        return DeviceStatus(
            isPhoneSilent = statusDto.isDeviceSilent,
            batteryPercentage = statusDto.batteryPercentage,
            time = statusDto.updateTime
        )
    }

    private fun getActivityStatus(statusDto: StatusDto): ActivityStatus? {
        if (statusDto.activityType.isNullOrBlank().not()) {
            return ActivityStatus(
                type = ActivityType.valueOf(statusDto.activityType?:""),
                time = statusDto.activityTime?:0L
            )
        }

        return null
    }
}