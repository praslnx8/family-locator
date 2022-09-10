package app.family.presentation.models

data class LoginState(
    val loggedIn: Boolean = false,
    val name: String? = null,
    val isFetching: Boolean = true
)