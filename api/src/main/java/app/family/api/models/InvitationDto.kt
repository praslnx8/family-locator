package app.family.api.models

import com.google.firebase.database.PropertyName

data class InvitationDto(
    @get:PropertyName("family_id")
    val familyId: String,
    @get:PropertyName("family_password")
    val familyPassword: String
) {
    constructor() : this("", "")
}