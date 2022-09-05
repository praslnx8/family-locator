package app.family.locator.ui.screens

import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.presentation.models.LoginState
import app.family.presentation.vms.LoginViewModel

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState = viewModel.getLoginState().collectAsState(initial = LoginState())

    Surface {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.logo)
        )
    }

    if (loginState.value.loggedIn) {
        onLogin()
    }
}