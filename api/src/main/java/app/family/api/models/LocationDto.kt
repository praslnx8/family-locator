package app.family.api.models

data class LocationDto(
    val lat: Double,
    val lon: Double
) {
    constructor() : this(0.0, 0.0)
}