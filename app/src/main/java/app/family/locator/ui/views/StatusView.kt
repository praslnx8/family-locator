package app.family.locator.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import app.family.domain.models.status.ActivityType
import app.family.locator.R
import app.family.presentation.models.StatusState

@Composable
fun StatusView(statusState: StatusState) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.default_padding))
            .fillMaxWidth()
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.default_padding))
                .fillMaxSize()
        ) {
            val (nameText, activityText, localityText, deviceLayout) = createRefs()

            Text(
                text = statusState.name,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.constrainAs(nameText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )
            if (statusState.isActivityValid()) {
                val verb = if (statusState.isActivityInPast()) "was" else "is"
                val activity = when (statusState.activityType) {
                    ActivityType.WALKING -> "Walking"
                    ActivityType.RUNNING -> "Running"
                    ActivityType.DRIVING -> "Driving"
                    null -> ""
                }
                Text(text = " $verb $activity", modifier = Modifier.constrainAs(activityText) {
                    start.linkTo(nameText.end)
                    top.linkTo(nameText.top)
                    bottom.linkTo(nameText.bottom)
                })
            }
            if (statusState.isLocalityValid()) {
                Text(
                    text = "at ${statusState.locality}",
                    modifier = Modifier.constrainAs(localityText) {
                        start.linkTo(parent.start)
                        top.linkTo(nameText.bottom)
                    })
            }

            Column(modifier = Modifier.constrainAs(deviceLayout) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                if (statusState.isPhoneSilent) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_volume_off_24),
                        contentDescription = "Phone Silent"
                    )
                }
                if (statusState.batteryPercentage < 20) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_battery_alert_24),
                        contentDescription = "Phone Silent"
                    )
                }

            }
        }
    }
}