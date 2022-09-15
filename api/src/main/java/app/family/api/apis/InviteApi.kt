package app.family.api.apis

import app.family.api.models.InvitationDto
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InviteApi(private val inviteReference: DatabaseReference) {

    fun createInvite(inviteKey: String, familyId: String, password: String): Flow<Unit> =
        callbackFlow {
            inviteReference.child(inviteKey)
                .setValue(InvitationDto(familyId, password)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(Unit)
                        close()
                    } else {
                        close(it.exception)
                    }
                }

            awaitClose()
        }

    fun getInviteDetails(inviteKey: String): Flow<InvitationDto> = callbackFlow {
        inviteReference.child(inviteKey).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val invitationDto = it.result.getValue(InvitationDto::class.java)
                if (invitationDto != null) {
                    trySend(invitationDto)
                    close()
                } else {
                    close(Exception("Invitation not found"))
                }
            } else {
                close(it.exception)
            }
        }

        awaitClose()
    }
}