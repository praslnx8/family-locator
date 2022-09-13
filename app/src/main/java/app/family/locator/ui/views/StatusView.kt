package app.family.locator.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import app.family.domain.models.status.ActivityType
import app.family.domain.models.status.WeatherType
import app.family.locator.R
import app.family.locator.ui.UIUtils
import app.family.presentation.models.StatusState
import java.util.concurrent.TimeUnit

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
                .fillMaxWidth()
        ) {
            val (avatarView, statusLayout, deviceLayout) = createRefs()

            AvatarView(
                name = statusState.name,
                modifier = Modifier
                    .constrainAs(avatarView) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(
                        start = dimensionResource(id = R.dimen.small_padding),
                        end = dimensionResource(id = R.dimen.small_padding)
                    ),
            )

            Column(
                modifier = Modifier
                    .constrainAs(statusLayout) {
                        start.linkTo(avatarView.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = dimensionResource(id = R.dimen.default_padding)),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(
                        text = statusState.name,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                    if (statusState.isActivityValid()) {
                        val verb = if (statusState.isActivityInPast()) "was" else "is"
                        val activity = when (statusState.activityType) {
                            ActivityType.WALKING -> "Walking"
                            ActivityType.RUNNING -> "Running"
                            ActivityType.DRIVING -> "Driving"
                            null -> ""
                        }
                        Text(text = " $verb", style = MaterialTheme.typography.body1)
                        Text(
                            text = " $activity",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
                if (statusState.isLocalityValid()) {
                    Text(
                        text = "at ${statusState.locality}",
                        style = MaterialTheme.typography.body2
                    )
                }
                Text(
                    text = UIUtils.getRelativeTime(statusState.getStatusTime()),
                    style = MaterialTheme.typography.caption
                )
            }

            Column(
                modifier = Modifier.constrainAs(deviceLayout) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (statusState.isWeatherValid()) {
                    Icon(
                        painter = painterResource(
                            id = UIUtils.getWeatherIcon(
                                weatherType = statusState.weatherType ?: WeatherType.CLEAR, false
                            )
                        ),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Phone Silent",
                        tint = Color.Unspecified
                    )
                }
                if (statusState.isPhoneSilent) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_volume_off_24),
                        contentDescription = "Phone Silent",
                        tint = Color.Unspecified
                    )
                }
                if (statusState.batteryPercentage < 20) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_battery_alert_24),
                        contentDescription = "Phone Silent",
                        tint = Color.Unspecified
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun PreviewStatusView() {
    StatusView(
        statusState = StatusState(
            name = "Bala K",
            activityType = ActivityType.WALKING,
            activityTime = System.currentTimeMillis(),
            locality = "Chennai",
            locationTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2),
            isPhoneSilent = true,
            batteryPercentage = 10,
            weatherType = WeatherType.CLOUDY,
            weatherTime = System.currentTimeMillis(),
            time = System.currentTimeMillis()
        )
    )
}