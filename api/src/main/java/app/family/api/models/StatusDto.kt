package app.family.api.models

import com.google.firebase.database.PropertyName

data class StatusDto(

    @get:PropertyName("lat")
    @set:PropertyName("lat")
    var lat: Double? = null,
    @get:PropertyName("lon")
    @set:PropertyName("lon")
    var lon: Double? = null,
    @get:PropertyName("locality")
    @set:PropertyName("locality")
    var locality: String? = null,
    @get:PropertyName("location_time")
    @set:PropertyName("location_time")
    var locationTime: Long? = null,

//Weather
    @get:PropertyName("temperature")
    @set:PropertyName("temperature")
    var temperature: Double? = null,
    @get:PropertyName("weather_type")
    @set:PropertyName("weather_type")
    var weatherType: String? = null,
    @get:PropertyName("weather_time")
    @set:PropertyName("weather_time")
    var weatherTime: Long? = null,

//Activity
    @get:PropertyName("activity_type")
    @set:PropertyName("activity_type")
    var activityType: String? = null,
    @get:PropertyName("is_online")
    @set:PropertyName("is_online")
    var isOnline: Boolean? = null,
    @get:PropertyName("activity_time")
    @set:PropertyName("activity_time")
    var activityTime: Long? = null,

//Phone Status
    @get:PropertyName("is_device_silent")
    @set:PropertyName("is_device_silent")
    var isDeviceSilent: Boolean = false,
    @get:PropertyName("battery_percentage")
    @set:PropertyName("battery_percentage")
    var batteryPercentage: Int = 0,

//Overall Data
    @get:PropertyName("update_time")
    @set:PropertyName("update_time")
    var updateTime: Long = 0L,
)