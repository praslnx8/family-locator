package app.family.locator.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import app.family.domain.models.status.ActivityType
import app.family.domain.usecases.MyStatusSyncUseCase
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActivityTransitionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var myStatusSyncUseCase: MyStatusSyncUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ActivityTransitionResult.hasResult(intent)) {
            Log.i("Activity Detection", "Detected Activity")
            val result = intent?.let { ActivityTransitionResult.extractResult(intent) }
            result?.let {
                result.transitionEvents.forEach { event ->
                    Log.i("Activity Detection", "Detected Activity " + event.activityType)
                    if (event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER) {
                        val activityType = when (event.activityType) {
                            DetectedActivity.ON_FOOT -> ActivityType.WALKING
                            DetectedActivity.ON_BICYCLE -> ActivityType.DRIVING
                            DetectedActivity.IN_VEHICLE -> ActivityType.DRIVING
                            DetectedActivity.RUNNING -> ActivityType.RUNNING
                            else -> null
                        }
                        if (activityType != null) {
                            myStatusSyncUseCase.syncActivityDetection(activityType)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun requestForActivityDetection(context: Context) {
            ActivityRecognition.getClient(context)
                .requestActivityTransitionUpdates(
                    ActivityTransitionRequest(getActivityTransitionRequest()),
                    getPendingIntent(context)
                )
        }

        private fun getActivityTransitionRequest(): List<ActivityTransition> {
            return mutableListOf<ActivityTransition>().apply {
                add(
                    ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build()
                )
                add(
                    ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build()
                )
                add(
                    ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build()
                )
                add(
                    ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_BICYCLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build()
                )
            }
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, ActivityTransitionReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}