package app.family.locator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.presentation.models.MyStatusState
import app.family.presentation.vms.MyStatusViewModel

@Composable
fun MyStatusScreen(viewModel: MyStatusViewModel = hiltViewModel()) {

    val myStatusState = viewModel.getMyStatus().collectAsState(initial = MyStatusState())
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.default_padding))
            .fillMaxWidth()
    ) {

        Box(modifier = Modifier.padding(dimensionResource(R.dimen.default_padding))) {
            Text(
                text = myStatusState.value.name ?: "No Name",
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}