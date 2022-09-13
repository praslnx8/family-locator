package app.family.locator.ui.screens.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.ui.views.StatusView
import app.family.presentation.models.StatusState
import app.family.presentation.vms.MyStatusViewModel

@Composable
fun MyStatusView(viewModel: MyStatusViewModel = hiltViewModel()) {
    val statusState = viewModel.getMyStatus().collectAsState(initial = StatusState())
    StatusView(statusState = statusState.value)
}