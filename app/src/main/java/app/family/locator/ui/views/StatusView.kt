package app.family.locator.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.default_padding))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarView(
                name = statusState.name,
                modifier = Modifier,
            )

            Column(
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.default_padding))
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(
                        text = statusState.name,
                        style = MaterialTheme.typography.bodyMedium,
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
                        Text(text = " $verb", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = " $activity",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (statusState.isLocalityValid()) {
                    Text(
                        text = "at ${statusState.locality}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = UIUtils.getRelativeTime(statusState.getStatusTime()),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Column(
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