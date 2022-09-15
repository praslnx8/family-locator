package app.family.api.apis

import app.family.api.models.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class AuthApi(private val auth: FirebaseAuth) {

    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getUser(): Flow<UserDto> = flow {
        val firebaseUser = auth.currentUser ?: throw Exception("User not found")
        emit(convertToUserDto(firebaseUser))
    }

    fun setName(name: String): Flow<Unit> = callbackFlow {
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        )?.addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(Unit)
                close()
            } else {
                close(it.exception)
            }
        }
        awaitClose { }
    }

    fun signIn(): Flow<UserDto> = callbackFlow {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                if (user != null) {
                    trySend(convertToUserDto(user))
                    close()
                } else {
                    close(Exception("User not found"))
                }
            } else {
                close(task.exception)
            }
        }
        awaitClose { }
    }

    private fun convertToUserDto(firebaseUser: FirebaseUser): UserDto {
        return UserDto(id = firebaseUser.uid, name = firebaseUser.displayName)
    }
}