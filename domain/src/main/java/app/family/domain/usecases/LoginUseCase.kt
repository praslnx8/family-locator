package app.family.domain.usecases

import app.family.api.apis.AuthApi
import app.family.api.models.UserDto
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUseCase(private val authApi: AuthApi) {

    fun login(): Flow<User> {
        val authFlow = if (authApi.isSignedIn()) {
            authApi.getUser()
        } else {
            authApi.signIn()
        }
        return authFlow.map {
            mapToUser(it)
        }
    }

    fun setName(name: String): Flow<Unit> {
        return authApi.setName(name)
    }

    private fun mapToUser(userDTO: UserDto): User {
        return User(id = userDTO.id, name = userDTO.name)
    }
}