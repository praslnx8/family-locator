package app.family.domain.usecases

import app.family.api.apis.UserApi
import app.family.api.models.UserDto
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val userApi: UserApi) {

    fun login(): Flow<User?> = flow {
        val userDTO = userApi.getUser().firstOrNull() ?: userApi.signIn().firstOrNull()
        emit(userDTO?.let { mapToUser(it) })
    }

    fun setName(name: String): Flow<Boolean> {
        return userApi.setName(name)
    }

    private fun mapToUser(userDTO: UserDto): User {
        return User(id = userDTO.id, name = userDTO.name)
    }
}