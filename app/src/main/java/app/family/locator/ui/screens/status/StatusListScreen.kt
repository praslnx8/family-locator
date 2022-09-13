package app.family.locator.ui.screens.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.ui.views.InvitationDialogView
import app.family.presentation.vms.HomeViewModel

@Composable
fun StatusListScreen(
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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MyStatusView()
            FamilyStatusView()
        }
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