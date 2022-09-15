package app.family.locator.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import app.family.domain.models.status.ActivityType
import app.family.domain.usecases.MyStatusSyncUseCase
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActivityTransitionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var myStatusSyncUseCase: MyStatusSyncUseCase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Activity Detection", "Detected Activity")
        if (intent != null && ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            val activityType = when (result?.mostProbableActivity?.type ?: 0) {
                DetectedActivity.ON_FOOT -> ActivityType.WALKING
                DetectedActivity.ON_BICYCLE -> ActivityType.DRIVING
                DetectedActivity.IN_VEHICLE -> ActivityType.DRIVING
                DetectedActivity.RUNNING -> ActivityType.RUNNING
                else -> null
            }
            if (activityType != null) {
                Log.i("Activity Detection", "Detected Activity $activityType")
                GlobalScope.launch {
                    myStatusSyncUseCase.syncActivityDetection(activityType)
                        .catch { e -> Log.e("Receiver", e.message ?: "") }
                        .collect()
                }
            }
        }
    }

    companion object {

        const val INTENT_ACTION = "action.activity_detection"

        fun getPendingIntent(context: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                111,
                Intent(INTENT_ACTION),
                PendingIntent.FLAG_MUTABLE
            )
        }
    }
}