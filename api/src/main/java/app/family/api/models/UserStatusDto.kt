package app.family.api.models

import com.google.firebase.database.PropertyName

data class UserStatusDto(
    @get:PropertyName("name")
    val name: String,
    @get:PropertyName("status")
    val statusDto: StatusDto
) {
    constructor() : this("", StatusDto())
}