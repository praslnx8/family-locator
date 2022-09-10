package app.family.api.apis

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class FamilyApi(private val database: FirebaseDatabase) {

    fun createFamily(familyId: String, password: String): Flow<Unit> = flow {
        val familyRef = database.getReference("families").child(familyId)
        familyRef.child("password").setValue(password)
        familyRef.push()
        emit(Unit)
    }

    fun joinFamily(userId: String, familyId: String, password: String): Flow<Unit> = flow {
        val userRef = database.getReference("users").child(userId)
        userRef.child("family_id").setValue(familyId)
        userRef.child("family_password").setValue(password)
        emit(Unit)
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
}