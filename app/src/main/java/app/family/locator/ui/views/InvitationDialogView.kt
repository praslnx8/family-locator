package app.family.locator.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.locator.utils.ShareUtils
import app.family.locator.utils.dashedBorder
import app.family.presentation.vms.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationDialogView(
    onDismiss: () -> Unit,
    viewModel: InvitationViewModel = hiltViewModel()
) {
    val invitationViewState = viewModel.invitationViewState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getInviteKey()
    }

    if (invitationViewState.value.isJoinedFamily) {
        viewModel.onExit()
        onDismiss()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        ConstraintLayout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    dimensionResource(id = R.dimen.default_padding)
                )
        ) {
            val (inviteHeading, inviteKey, shareBtn, dividerLayout, joinHeading, inviteEditText, joinBtn) = createRefs()
            val inviteKeyState = rememberSaveable(Unit) { mutableStateOf("") }

            Text(
                text = "Invite your Family",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.constrainAs(inviteHeading) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Box(modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_padding))
                .dashedBorder(
                    2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = MaterialTheme.shapes.medium,
                    on = 4.dp,
                    off = 4.dp
                )
                .constrainAs(inviteKey) {
                    top.linkTo(inviteHeading.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                if (invitationViewState.value.isFetchingInviteKey) {
                    CircularProgressIndicator(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding)))
                } else {
                    Text(
                        text = invitationViewState.value.inviteKey,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding))
                    )
                }

            }

            OutlinedButton(
                onClick = {
                    ShareUtils.shareInviteKey(context, invitationViewState.value.inviteKey)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(shareBtn) {
                        top.linkTo(inviteKey.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = "Share", style = MaterialTheme.typography.labelLarge)
            }
            Row(
                modifier = Modifier.constrainAs(dividerLayout) {
                    top.linkTo(shareBtn.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                )
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.default_padding)
                    )
                )
                Divider(
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                )
            }
            Text(
                text = "Join with Invite Link",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.constrainAs(joinHeading) {
                    top.linkTo(dividerLayout.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            TextField(
                label = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Enter Invite Key")
                    }
                },
                value = inviteKeyState.value,
                onValueChange = {
                    inviteKeyState.value = it
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (inviteKeyState.value.isNotBlank()) {
                        viewModel.joinFamily(inviteKeyState.value)
                    }
                }),
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.default_padding))
                    .constrainAs(inviteEditText) {
                        top.linkTo(joinHeading.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            if (invitationViewState.value.isJoiningFamily) {
                CircularProgressIndicator(modifier = Modifier.constrainAs(joinBtn) {
                    top.linkTo(inviteEditText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            } else {
                FilledTonalButton(
                    onClick = { viewModel.joinFamily(inviteKeyState.value) },
                    enabled = inviteKeyState.value.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(joinBtn) {
                            top.linkTo(inviteEditText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(text = "Join", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewInvitationDialog() {
    InvitationDialogView({})
}