package app.family.locator.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.locator.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StatusSyncService : Service() {

    @Inject
    lateinit var myStatusSyncUseCase: MyStatusSyncUseCase

    @Inject
    lateinit var notificationUtils: NotificationUtils

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        scope.launch {
            myStatusSyncUseCase.listenAndSync().collect()
        }

        startForeground()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForeground() {
        startForeground(1, notificationUtils.getForegroundNotification())
    }

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, StatusSyncService::class.java)
            context.startForegroundService(intent)
        }
    }
}