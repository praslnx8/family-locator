package app.family.api.models

import com.google.firebase.database.PropertyName

data class UserStatusDto(
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",
    @get:PropertyName("status")
    @set:PropertyName("status")
    var statusDto: StatusDto = StatusDto()
)