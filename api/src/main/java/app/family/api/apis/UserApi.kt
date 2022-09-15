package app.family.api.apis

import app.family.api.models.InvitationDto
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserApi(private val userReference: DatabaseReference) {

    fun joinFamily(userId: String, familyId: String, password: String): Flow<Unit> =
        callbackFlow {
            userReference.child(userId).setValue(InvitationDto(familyId, password))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(Unit)
                        close()
                    } else {
                        close(it.exception)
                    }
                }
            awaitClose()
        }


    fun getFamilyId(userId: String): Flow<String?> = callbackFlow {
        userReference.child(userId).child("family_id").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val familyId = it.result.value as? String
                if (familyId != null) {
                    trySend(familyId)
                    close()
                } else {
                    trySend(null)
                    close()
                }
            } else {
                close(it.exception)
            }
        }
        awaitClose()
    }

    fun getFamilyPassword(userId: String): Flow<String> = callbackFlow {
        userReference.child(userId).child("family_password").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val password = it.result.value as? String
                if (password != null) {
                    trySend(password)
                    close()
                } else {
                    close()
                }
            } else {
                close(it.exception)
            }
        }
        awaitClose()
    }
}