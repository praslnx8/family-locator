package app.family.api.apis

import app.family.api.models.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class UserApi(private val auth: FirebaseAuth) {

    fun getUser(): Flow<UserDTO?> = flow {
        emit(auth.currentUser?.let { convertToUserDto(it) })
    }

    fun setName(name: String): Flow<Boolean> = callbackFlow {
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        )?.addOnCompleteListener {
            trySend(it.isSuccessful)
        }
        awaitClose{  }
    }

    fun signIn(): Flow<UserDTO?> = callbackFlow {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(auth.currentUser?.let { convertToUserDto(it) })
            } else {
                trySend(null)
            }
        }
        awaitClose{  }
    }

    private fun convertToUserDto(firebaseUser: FirebaseUser): UserDTO {
        return UserDTO(id = firebaseUser.uid, name = firebaseUser.displayName)
    }
}