package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.MessageUseCase
import app.family.domain.usecases.UserUseCase
import app.family.presentation.models.MessageState
import app.family.presentation.models.MessageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageUseCase: MessageUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun listenToMessage(): Flow<MessageViewState> {
        return userUseCase.getUser().flatMapMerge { user ->
            messageUseCase.listenToChat().map { messages ->
                MessageViewState(messages.map { message ->
                    MessageState(
                        name = message.senderName,
                        message = message.message,
                        time = message.time,
                        isCurrentUser = message.senderId == user.id
                    )
                })
            }
        }.catch { e -> Timber.e(e) }
    }

    fun addMessage(message: String) {
        viewModelScope.launch {
            messageUseCase.sendMessage(message)
                .catch { e -> Timber.e(e) }
                .collect()
        }
    }
}