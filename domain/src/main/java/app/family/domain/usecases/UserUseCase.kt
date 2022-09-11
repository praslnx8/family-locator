package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserUseCase(private val authApi: AuthApi) {

    fun getUser(): Flow<User?> {
        return authApi.getUser().map {
            it?.let { User(it.id, it.name?.ifBlank { null }) }
        }
    }
}