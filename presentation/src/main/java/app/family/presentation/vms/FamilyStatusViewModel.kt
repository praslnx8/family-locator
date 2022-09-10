package app.family.presentation.vms

import androidx.lifecycle.ViewModel
import app.family.presentation.models.StatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FamilyStatusViewModel @Inject constructor(
) : ViewModel() {

    fun getMyStatus(): Flow<StatusState> {
        TODO()
    }
}