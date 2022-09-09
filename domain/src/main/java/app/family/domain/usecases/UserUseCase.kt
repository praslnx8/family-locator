package app.family.domain.usecases

import app.family.api.apis.UserApi
import app.family.domain.exceptions.NotLoggedInException
import app.family.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserUseCase(private val userApi: UserApi) {

    fun getUser(): Flow<User> {
        return userApi.getUser().map {
            if (it == null) {
                throw NotLoggedInException()
            }
            User(it.id, it.name?.ifBlank { null })
        }
    }
}