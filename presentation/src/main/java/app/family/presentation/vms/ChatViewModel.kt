package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.MessageUseCase
import app.family.presentation.models.MessageState
import app.family.presentation.models.MessageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val messageUseCase: MessageUseCase) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun listenToMessage(): Flow<MessageViewState> {
        return messageUseCase.listenToChat().map {
            MessageViewState(it.map { message ->
                MessageState(
                    name = message.senderName,
                    message = message.message,
                    time = message.time
                )
            })
        }
    }

    fun addMessage(message: String) {
        viewModelScope.launch {
            messageUseCase.sendMessage(message).collect()
        }
    }
}