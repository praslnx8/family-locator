package app.family.presentation.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import app.family.domain.usecases.UploadStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val uploadStatusUseCase: UploadStatusUseCase
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun pushFamilyStatus() {
        viewModelScope.launch {
            uploadStatusUseCase.uploadMyStatus()
                .catch { e -> Timber.e(e) }
                .collect()
        }
    }
}