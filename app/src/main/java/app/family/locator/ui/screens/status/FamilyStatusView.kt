package app.family.locator.ui.screens.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.ui.views.StatusView
import app.family.presentation.vms.FamilyStatusViewModel

@Composable
fun FamilyStatusView(viewModel: FamilyStatusViewModel = hiltViewModel()) {
    val statusListState = viewModel.getFamilyStatuses().collectAsState(initial = emptyList())
    Column {
        Divider(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
        )
        statusListState.value.forEach { statusState ->
            StatusView(statusState = statusState)
        }
    }

}