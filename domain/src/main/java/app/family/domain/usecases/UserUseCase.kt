package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class UserUseCase(private val authApi: AuthApi) {

    fun isSignedIn(): Flow<Boolean> {
        return flowOf(authApi.isSignedIn())
    }

    fun getUser(): Flow<User> {
        return authApi.getUser().map {
            User(it.id, it.name?.ifBlank { null })
        }
    }
}