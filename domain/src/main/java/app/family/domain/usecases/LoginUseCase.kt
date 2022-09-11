package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.models.UserDto
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val authApi: AuthApi) {

    fun login(): Flow<User?> = flow {
        val userDTO = authApi.getUser().firstOrNull() ?: authApi.signIn().firstOrNull()
        emit(userDTO?.let { mapToUser(it) })
    }

    fun setName(name: String): Flow<Boolean> {
        return authApi.setName(name)
    }

    private fun mapToUser(userDTO: UserDto): User {
        return User(id = userDTO.id, name = userDTO.name)
    }
}