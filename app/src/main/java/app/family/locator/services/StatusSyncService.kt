package app.family.locator.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.family.domain.usecases.StatusSyncUseCase
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
    lateinit var statusSyncUseCase: StatusSyncUseCase
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        scope.launch {
            statusSyncUseCase.listenAndSync().collect()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}