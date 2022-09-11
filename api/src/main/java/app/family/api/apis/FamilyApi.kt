package app.family.api.apis

import android.util.Log
import app.family.api.models.StatusDto
import app.family.api.models.UserStatusDto
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FamilyApi(private val familyReference: DatabaseReference) {

    fun createFamily(familyId: String, password: String): Flow<Boolean> = callbackFlow {
        familyReference.child(familyId).child("password").setValue(password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Family Api", "Success creating Family")
            } else {
                Log.e("Family API", "Error creating Family " + it.exception?.message)
            }
            trySend(it.isSuccessful)
        }
        awaitClose()
    }

    fun pushStatusToFamily(
        familyId: String,
        userId: String,
        name: String,
        statusDto: StatusDto
    ): Flow<Boolean> = callbackFlow {
        familyReference.child(familyId).child("statuses").child(userId)
            .setValue(UserStatusDto(name, statusDto)).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("Family API", "Success pushing status")
                } else {
                    Log.e("Family API", "Failure in pushing status " + it.exception?.message)
                }
                trySend(it.isSuccessful)
            }
        awaitClose()
    }
}