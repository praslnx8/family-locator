package app.family.presentation.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import app.family.domain.usecases.UserUseCase
import app.family.presentation.models.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    fun getProfile() {
        viewModelScope.launch {
            userUseCase.getUser()
                .catch { e -> Log.e("ProfileViewModel", e.message ?: "") }
                .collect { user ->
                    _profileState.emit(ProfileState(user.name ?: ""))
                }
        }
    }

}