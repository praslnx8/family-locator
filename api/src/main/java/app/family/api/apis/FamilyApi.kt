package app.family.api.apis

import app.family.api.models.InvitationDto
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FamilyApi(private val database: FirebaseDatabase) {

    fun createFamily(familyId: String, password: String): Flow<Boolean> = callbackFlow {
        val familyRef = database.getReference("families").child(familyId)
        familyRef.child("password").setValue(password).addOnCompleteListener {
            trySend(it.isSuccessful)
        }
        awaitClose()
    }

    fun joinFamily(userId: String, familyId: String, password: String): Flow<Boolean> =
        callbackFlow {
            database.getReference("users").child(userId).setValue(InvitationDto(familyId, password))
                .addOnCompleteListener {
                    trySend(it.isSuccessful)
                }
            awaitClose()
        }

    fun getFamilyId(userId: String): Flow<String?> = callbackFlow {
        val userRef = database.getReference("users").child(userId)
        userRef.child("family_id").get().addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(it.result.value as? String)
            } else {
                trySend(null)
            }
        }
        awaitClose { }
    }

    fun getFamilyPassword(userId: String): Flow<String?> = callbackFlow {
        val userRef = database.getReference("users").child(userId)
        userRef.child("family_password").get().addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(it.result.value as? String)
            } else {
                trySend(null)
            }
        }
        awaitClose { }
    }
}