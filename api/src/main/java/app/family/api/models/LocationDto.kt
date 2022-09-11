package app.family.api.models

import com.google.firebase.database.PropertyName

data class LocationDto(
    @get:PropertyName("lat")
    @set:PropertyName("lat")
    var lat: Double = 0.0,
    @get:PropertyName("lon")
    @set:PropertyName("lon")
    var lon: Double = 0.0
)