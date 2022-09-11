package app.family.api.apis

import android.util.Log
import app.family.api.models.InvitationDto
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserApi(private val userReference: DatabaseReference) {

    fun joinFamily(userId: String, familyId: String, password: String): Flow<Boolean> =
        callbackFlow {
            userReference.child(userId).setValue(InvitationDto(familyId, password))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("User APi", "Success joining Family")
                    } else {
                        Log.e("User APi", "Error joining family " + it.exception?.message)
                    }
                    trySend(it.isSuccessful)
                }
            awaitClose()
        }


    fun getFamilyId(userId: String): Flow<String?> = callbackFlow {
        userReference.child(userId).child("family_id").get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("User APi", "Success fetching family id")
                trySend(it.result.value as? String)
            } else {
                Log.e("User APi", "Error fetching family id" + it.exception?.message)
                trySend(null)
            }
        }
        awaitClose { }
    }

    fun getFamilyPassword(userId: String): Flow<String?> = callbackFlow {
        userReference.child(userId).child("family_password").get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("User APi", "Success fetching family password")
                trySend(it.result.value as? String)
            } else {
                Log.e("User APi", "Error fetching family password" + it.exception?.message)
                trySend(null)
            }
        }
        awaitClose { }
    }
}