package app.family.api.apis

import android.util.Log
import app.family.api.models.InvitationDto
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InviteApi(private val inviteReference: DatabaseReference) {

    fun createInvite(inviteKey: String, familyId: String, password: String): Flow<Boolean> =
        callbackFlow {
            inviteReference.child(inviteKey)
                .setValue(InvitationDto(familyId, password)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("Invite API", "Success creating Invite")
                    } else {
                        Log.e("Invite API", "Failure in creating Invite " + it.exception?.message)
                    }
                    trySend(it.isSuccessful)
                }

            awaitClose()
        }

    fun getInviteDetails(inviteKey: String): Flow<InvitationDto?> = callbackFlow {
        inviteReference.child(inviteKey).get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Invite API", "Success Fetching Invite Key " + it.result.value?.toString())
            } else {
                Log.e("Invite API", "Error fetching invite key " + it.exception?.message)
            }
            trySend(it.result.getValue(InvitationDto::class.java))
        }

        awaitClose()
    }
}