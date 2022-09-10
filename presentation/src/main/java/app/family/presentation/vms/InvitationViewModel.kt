package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.FamilyInviteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(private val inviteUseCase: FamilyInviteUseCase) :
    ViewModel() {

    private val _isFamilyJoinedState = MutableStateFlow(false)
    val isFamilyJoinedState: StateFlow<Boolean> = _isFamilyJoinedState

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getInviteKey(): Flow<String?> {
        return inviteUseCase.inviteFamily()
    }

    fun joinFamily(inviteKey: String) {
        viewModelScope.launch {
            _isFamilyJoinedState.emit(inviteUseCase.joinFamily(inviteKey).first())
        }
    }
}