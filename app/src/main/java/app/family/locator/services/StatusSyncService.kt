package app.family.locator.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.domain.usecases.UpdateFamilyStatusUseCase
import app.family.domain.usecases.UpdateMessageUseCase
import app.family.domain.usecases.UploadStatusUseCase
import app.family.locator.utils.NotificationUtils
import com.google.android.gms.location.ActivityRecognition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class StatusSyncService : Service() {

    @Inject
    lateinit var myStatusSyncUseCase: MyStatusSyncUseCase

    @Inject
    lateinit var uploadStatusUseCase: UploadStatusUseCase

    @Inject
    lateinit var updateFamilyStatusUseCase: UpdateFamilyStatusUseCase

    @Inject
    lateinit var updateMessageUseCase: UpdateMessageUseCase

    @Inject
    lateinit var notificationUtils: NotificationUtils

    private lateinit var activityTransitionReceiver: ActivityTransitionReceiver
    private lateinit var pendingIntent: PendingIntent
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        listenToActivityDetection()
        startForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        job?.cancel("Invalidate old ones")
        job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.IO + (job ?: SupervisorJob()))
        Log.i("Service", "On start called")
        scope.launch {
            myStatusSyncUseCase.listenAndSync().collect {
                Log.i("Service", "Listen and sync")
            }
        }
        scope.launch {
            uploadStatusUseCase.uploadMyStatus().collect() {
                Log.i("Service", "Upload status")
            }
        }
        scope.launch {
            updateFamilyStatusUseCase.listenAndUpdateFamilyStatus().collect() {
                Log.i("Service", "Listen and update family status")
            }
        }
        scope.launch {
            updateMessageUseCase.syncAndUpdateMessages().collect() {
                Log.i("Service", "Listen and update message")
            }
        }
        Log.i("Service", "Listening to all")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForeground() {
        startForeground(1, notificationUtils.getForegroundNotification())
    }

    private fun listenToActivityDetection() {
        activityTransitionReceiver = ActivityTransitionReceiver()
        registerReceiver(
            activityTransitionReceiver,
            IntentFilter(ActivityTransitionReceiver.INTENT_ACTION)
        )
        pendingIntent = ActivityTransitionReceiver.getPendingIntent(this)
        ActivityRecognition.getClient(this)
            .requestActivityUpdates(
                TimeUnit.MINUTES.toMillis(2),
                pendingIntent
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("Activity Recognition", "Success requesting")
                } else {
                    Log.e("Activity Recognition", "Error requesting " + it.exception?.message)
                }
            }
    }

    private fun stopActivityUpdates() {
        unregisterReceiver(activityTransitionReceiver)
        ActivityRecognition.getClient(this).removeActivityTransitionUpdates(pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopActivityUpdates()
    }

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, StatusSyncService::class.java)
            context.startForegroundService(intent)
        }
    }
}