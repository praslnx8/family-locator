package app.family.locator.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.locator.ui.views.AvatarView
import app.family.presentation.vms.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val profileState = viewModel.profileState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getProfile()
    }

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize(1f)
        ) {
            val (avatar, name) = createRefs()
            AvatarView(name = profileState.value.name, modifier = Modifier
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(top = 100.dp)
                .size(100.dp),
                textStyle = MaterialTheme.typography.headlineMedium)
            Text(
                text = profileState.value.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(avatar.bottom)
                    }
                    .padding(dimensionResource(id = R.dimen.small_padding)))
        }
    }

}