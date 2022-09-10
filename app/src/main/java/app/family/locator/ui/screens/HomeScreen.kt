package app.family.locator.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.family.locator.ui.views.InvitationDialogView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val inviteClicked = rememberSaveable(Unit) { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    inviteClicked.value = true
                },
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Invite Family Member")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        StatusListScreen()
        if (inviteClicked.value) {
            InvitationDialogView(
                onDismiss = { inviteClicked.value = false },
            )
        }
    }
}