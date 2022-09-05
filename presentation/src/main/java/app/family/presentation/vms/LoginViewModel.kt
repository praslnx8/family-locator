package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.LoginUseCase
import app.family.presentation.models.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    fun getLoginState(): Flow<LoginState> {
        return loginUseCase.login().map {
            LoginState(it != null)
        }
    }
}