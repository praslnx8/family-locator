package app.family.api.mappers

import app.family.api.models.StatusDto
import app.family.api.models.StatusProto
import app.family.api.models.UserStatusDto
import app.family.api.models.UserStatusProto

class StatusMapper {

    fun mapToStatusProto(userStatusDto: UserStatusDto): UserStatusProto {
        return UserStatusProto.newBuilder()
            .setName(userStatusDto.name)
            .setStatus(mapToStatusProto(userStatusDto.statusDto))
            .build()
    }

    private fun mapToStatusProto(statusDto: StatusDto): StatusProto {
        return StatusProto.newBuilder()
            .setLat(statusDto.lat ?: 0.0)
            .setLon(statusDto.lon ?: 0.0)
            .setLocality(statusDto.locality?:"")
            .setLocationTime(statusDto.locationTime ?: 0L)
            .setTemperature(statusDto.temperature ?: 0.0)
            .setWeatherType(statusDto.weatherType ?: 0)
            .setWeatherTime(statusDto.weatherTime ?: 0L)
            .setActivity(statusDto.activityType?:"")
            .setIsOnline(false)
            .setActivityTime(statusDto.activityTime ?: 0)
            .setIsPhoneSilent(statusDto.isDeviceSilent)
            .setBatteryPercentage(statusDto.batteryPercentage)
            .setUpdateTime(statusDto.updateTime)
            .build()
    }

    fun mapToStatusDto(statusProto: StatusProto): StatusDto {
        return StatusDto(
            lat = if (statusProto.lat != 0.0) statusProto.lat else null,
            lon = if (statusProto.lon != 0.0) statusProto.lon else null,
            locality = statusProto.locality.ifBlank { null },
            locationTime = if (statusProto.locationTime != 0L) statusProto.locationTime else null,
            temperature = if (statusProto.temperature != 0.0) statusProto.temperature else null,
            weatherType = if (statusProto.weatherType != 0) statusProto.weatherType else null,
            weatherTime = if (statusProto.weatherTime != 0L) statusProto.weatherTime else null,
            activityType = statusProto.activity.ifBlank { null },
            isOnline = statusProto.isOnline,
            activityTime = if (statusProto.activityTime != 0L) statusProto.activityTime else null,
            isDeviceSilent = statusProto.isPhoneSilent,
            batteryPercentage = statusProto.batteryPercentage,
            updateTime = statusProto.updateTime,
        )
    }

    fun mapToStatusDto(userStatusProto: UserStatusProto): UserStatusDto {
        return UserStatusDto(
            name = userStatusProto.name ?: "",
            statusDto = mapToStatusDto(userStatusProto.status)
        )
    }
}