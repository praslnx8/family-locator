package app.family.presentation.vms

import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import app.family.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {


    fun checkNavigateToHome(): Flow<Boolean> {
        return userUseCase.isSignedIn().flatMapMerge { isSignedIn ->
            if (isSignedIn) {
                userUseCase.getUser().map { user ->
                    user.name.isNullOrBlank().not()
                }
            } else {
                flowOf(false)
            }
        }

    }
}