package app.family.locator.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.presentation.models.LoginState
import app.family.presentation.vms.LoginViewModel

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState = viewModel.loginState.collectAsState(initial = LoginState())
    val nameState = rememberSaveable(Unit) { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.checkLogin()
    }

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (icon, nameText, button) = createRefs()

            if (loginState.value.isFetching) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.logo),
                    modifier = Modifier.constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                )
            } else {
                TextField(
                    modifier = Modifier.constrainAs(nameText) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (nameState.value.isNotBlank()) {
                            viewModel.setName(nameState.value)
                        }
                    }),
                    value = nameState.value,
                    onValueChange = {
                        nameState.value = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.add_nick_name),
                            style = MaterialTheme.typography.body2
                        )
                    },
                )
                Button(
                    modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(nameText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(paddingValues),
                    onClick = { viewModel.setName(nameState.value) },
                    enabled = nameState.value.isNotBlank(),
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = loginState.value) {
        if (loginState.value.loggedIn && loginState.value.name.isNullOrBlank().not()) {
            onLogin()
        }
    }
}