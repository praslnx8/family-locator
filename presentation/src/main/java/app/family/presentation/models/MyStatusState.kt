package app.family.presentation.models

import app.family.domain.models.status.ActivityType

data class MyStatusState(
    val name: String? = null,
    val activityType: ActivityType? = null,
    val activityTime: Long? = null,
    val locality: String? = null,
    val locationTime: Long? = null,
    val isPhoneSilent: Boolean = false,
    val batteryPercentage: Int = 0,
    val time: Long = 0L
)
