package app.family.api.models

import com.google.firebase.database.PropertyName

data class StatusDto(

    @get:PropertyName("lat")
    val lat: Double?,
    @get:PropertyName("lon")
    val lon: Double?,
    @get:PropertyName("locality")
    val locality: String?,
    @get:PropertyName("location_time")
    val locationTime: Long?,

//Weather
    @get:PropertyName("temperature")
    val temperature: Double?,
    @get:PropertyName("weather_type")
    val weatherType: String?,
    @get:PropertyName("weather_time")
    val weatherTime: Long?,

//Activity
    @get:PropertyName("activity_type")
    val activityType: String?,
    @get:PropertyName("is_online")
    val isOnline: Boolean?,
    @get:PropertyName("activity_time")
    val activityTime: Long?,

//Phone Status
    @get:PropertyName("is_device_silent")
    val isDeviceSilent: Boolean,
    @get:PropertyName("battery_percentage")
    val batteryPercentage: Int,

//Overall Data
    @get:PropertyName("update_time")
    val updateTime: Long,
) {
    constructor() : this(null,null,null,null,null,null,null,null,null,null,false,0,0L)
}