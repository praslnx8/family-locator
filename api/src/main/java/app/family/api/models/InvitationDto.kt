package app.family.api.models

import com.google.firebase.database.PropertyName

data class InvitationDto(
    @get:PropertyName("family_id")
    @set:PropertyName("family_id")
    var familyId: String = "",
    @get:PropertyName("family_password")
    @set:PropertyName("family_password")
    var familyPassword: String = ""
)