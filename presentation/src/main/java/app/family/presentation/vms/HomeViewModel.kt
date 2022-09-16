package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.domain.usecases.MessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val messageUseCase: MessageUseCase
) : ViewModel() {

    fun getUnReadMessageCount(): Flow<Int> {
        return messageUseCase.getUnReadMessages().map {
            it.count()
        }
    }
}