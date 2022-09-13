package app.family.locator.ui.screens

import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.ui.views.InvitationDialogView
import app.family.presentation.vms.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val inviteClicked = rememberSaveable(Unit) { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    inviteClicked.value = true
                },
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Invite Family Member")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        StatusListScreen()
        if (inviteClicked.value) {
            InvitationDialogView(
                onDismiss = {
                    inviteClicked.value = false
                },
                onJoined = {
                    viewModel.pushFamilyStatus()
                }
            )
        }
    }
}