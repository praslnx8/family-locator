package app.family.api.apis

import app.family.api.models.InvitationDto
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InviteApi(private val database: FirebaseDatabase) {

    fun createInvite(inviteKey: String, familyId: String, password: String): Flow<Boolean> =
        callbackFlow {
            database.getReference(INVITATIONS_PATH).child(inviteKey)
                .setValue(InvitationDto(familyId, password)).addOnCompleteListener {
                    trySend(it.isSuccessful)
                }

            awaitClose()
        }

    fun getInviteDetails(inviteKey: String): Flow<InvitationDto?> = callbackFlow {
        database.getReference(INVITATIONS_PATH).child(inviteKey).get().addOnCompleteListener {
            trySend(it.result.getValue(InvitationDto::class.java))
        }

        awaitClose()
    }

    companion object {
        private const val INVITATIONS_PATH = "invitations"
    }
}