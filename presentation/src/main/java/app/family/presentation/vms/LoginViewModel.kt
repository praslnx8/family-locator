package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.LoginUseCase
import app.family.presentation.models.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val loginState: Flow<LoginState> = _loginState

    fun checkLogin() {
        viewModelScope.launch {
            loginUseCase.login()
                .onStart {
                    _loginState.emit(LoginState(isFetching = true))
                }.collect {
                    _loginState.emit(
                        LoginState(
                            loggedIn = true,
                            name = it.name?.ifBlank { null },
                            isFetching = false
                        )
                    )
                }
        }

    }

    fun setName(name: String) {
        viewModelScope.launch {
            loginUseCase.setName(name)
                .onStart {
                    _loginState.emit(
                        LoginState(isFetching = true)
                    )
                }.catch { e -> Timber.e(e) }
                .onCompletion {
                    checkLogin()
                }.collect()
        }

    }
}