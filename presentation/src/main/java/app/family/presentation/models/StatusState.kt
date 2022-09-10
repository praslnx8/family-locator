package app.family.presentation.models

import app.family.domain.models.status.ActivityType
import java.util.concurrent.TimeUnit

data class StatusState(
    val name: String = "",
    val activityType: ActivityType? = null,
    val activityTime: Long? = null,
    val locality: String? = null,
    val locationTime: Long? = null,
    val isPhoneSilent: Boolean = false,
    val batteryPercentage: Int = 0,
    val time: Long = 0L
) {
    fun isActivityValid() =
        activityType != null && (System.currentTimeMillis() - (activityTime
            ?: 0)) < TimeUnit.MINUTES.toMillis(30)

    fun isActivityInPast() =
        (System.currentTimeMillis() - (activityTime ?: 0)) > TimeUnit.MINUTES.toMillis(10)

    fun isLocalityValid() =
        locality.isNullOrBlank().not() && (System.currentTimeMillis() - (locationTime
            ?: 0)) < TimeUnit.MINUTES.toMillis(30)
}
