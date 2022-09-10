package app.family.api.apis

import app.family.api.models.StatusDto
import app.family.api.models.UserStatusDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class FamilyStatusApi(private val database: FirebaseDatabase) {

    fun pushStatus(
        userId: String,
        name: String,
        familyId: String,
        statusDto: StatusDto
    ): Flow<Boolean> = callbackFlow {
        database.getReference("families").child(familyId).child("statuses").child(userId)
            .setValue(UserStatusDto(name, statusDto)).addOnCompleteListener {
                trySend(it.isSuccessful)
            }
        awaitClose()
    }

    fun listenToFamilyStatus(familyId: String): Flow<Unit> = flow {
        database.getReference("families").child(familyId).child("statuses")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //
                }

                override fun onCancelled(error: DatabaseError) {
                    //
                }

            })
    }
}