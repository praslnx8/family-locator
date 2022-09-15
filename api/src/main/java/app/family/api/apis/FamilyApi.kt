package app.family.api.apis

import android.util.Log
import androidx.datastore.core.DataStore
import app.family.api.mappers.StatusMapper
import app.family.api.models.StatusCollectionProto
import app.family.api.models.StatusDto
import app.family.api.models.UserStatusDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FamilyApi(
    private val familyReference: DatabaseReference,
    private val statusCollectionDataStore: DataStore<StatusCollectionProto>,
    private val statusMapper: StatusMapper
) {

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

    fun listenToFamilyUpdates(familyId: String): Flow<Map<String, UserStatusDto>> = callbackFlow {
        val statusReference = familyReference.child(familyId).child("statuses")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("Family API", "Data change occured")
                val userStatusMap = mutableMapOf<String, UserStatusDto>()
                snapshot.children.forEach { dataSnapShot ->
                    val value = dataSnapShot.getValue(UserStatusDto::class.java)
                    val key = dataSnapShot.key
                    if (value != null) {
                        userStatusMap[key.toString()] = value
                    }
                }
                trySend(userStatusMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Family API", "family status update cancelled " + error.message)
                close()
            }

        }
        statusReference.addValueEventListener(valueEventListener)
        awaitClose {
            Log.w("Family API", "Family status listen closed")
            statusReference.removeEventListener(valueEventListener)
        }
    }

    fun storeFamilyUpdates(userStatusMap: Map<String, UserStatusDto>): Flow<Unit> = flow {
        val statusMap = userStatusMap.mapValues {
            statusMapper.mapToStatusProto(it.value)
        }
        statusCollectionDataStore.updateData { oldData ->
            oldData.toBuilder()
                .clearUserStatuses()
                .putAllUserStatuses(statusMap)
                .build()
        }
        emit(Unit)
    }

    fun fetchFamilyUpdates(): Flow<Map<String, UserStatusDto?>> {
        return statusCollectionDataStore.data.map { statusCollectionProto ->
            statusCollectionProto.userStatusesMap.mapValues {
                statusMapper.mapToStatusDto(it.value)
            }
        }
    }
}