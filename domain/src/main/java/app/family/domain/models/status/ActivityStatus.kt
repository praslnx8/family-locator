package app.family.domain.models.status

data class ActivityStatus(
    val type: ActivityType,
    val time: Long
) {
}