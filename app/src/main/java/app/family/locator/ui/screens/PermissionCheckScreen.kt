import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import app.family.locator.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionCheckScreen(
    onPermissionsGranted: () -> Unit,
) {
    val permissionStates =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

    LaunchedEffect(key1 = permissionStates.allPermissionsGranted) {
        if (permissionStates.allPermissionsGranted) {
            onPermissionsGranted()
        }
    }


    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (text, button) = createRefs()
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.request_permission_message),
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
            Button(onClick = {
                if (permissionStates.allPermissionsGranted) {
                    onPermissionsGranted()
                } else {
                    permissionStates.launchMultiplePermissionRequest()
                }
            },
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(text = stringResource(R.string.grant_permission_button))
            }
        }
    }
}