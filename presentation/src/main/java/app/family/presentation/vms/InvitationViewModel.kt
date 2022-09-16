package app.family.presentation.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import app.family.domain.usecases.FamilyInviteUseCase
import app.family.presentation.models.InvitationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(private val inviteUseCase: FamilyInviteUseCase) :
    ViewModel() {

    private val _invitationViewState = MutableStateFlow(InvitationViewState())
    val invitationViewState: StateFlow<InvitationViewState> = _invitationViewState

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getInviteKey() {
        viewModelScope.launch {
            inviteUseCase.inviteFamily()
                .onStart {
                    _invitationViewState.emit(invitationViewState.value.copy(isFetchingInviteKey = true))
                }.onEach {
                    _invitationViewState.emit(
                        invitationViewState.value.copy(
                            isFetchingInviteKey = false,
                            inviteKey = it
                        )
                    )
                }.catch { e -> Timber.e(e) }
                .collect()
        }
    }

    fun joinFamily(inviteKey: String) {
        viewModelScope.launch {
            inviteUseCase.joinFamily(inviteKey)
                .onStart {
                    _invitationViewState.emit(invitationViewState.value.copy(isJoiningFamily = true))
                }.onCompletion {
                    _invitationViewState.emit(
                        invitationViewState.value.copy(
                            isJoiningFamily = false,
                            isJoinedFamily = true
                        )
                    )
                }.catch { e -> Timber.e(e) }
                .collect()
        }
    }

    fun onExit() {
        _invitationViewState.value = invitationViewState.value.copy(
            isJoiningFamily = false,
            isJoinedFamily = false
        )
    }
}