package app.family.locator.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.family.domain.usecases.MyStatusSyncUseCase
import app.family.domain.usecases.UploadStatusUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.time.Duration

class StatusSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    val syncUseCase: MyStatusSyncUseCase,
    val uploadUseCase: UploadStatusUseCase
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.i("Worker started")
        syncUseCase.syncNow()
            .catch { e -> Timber.e(e) }
            .collect()
        uploadUseCase.uploadMyStatus()
            .catch { e -> Timber.e(e) }
            .collect()
        StatusSyncService.startService(appContext)
        Timber.i("Worker Finished")
        return Result.success()
    }

    companion object {
        fun startPeriodicWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "STATUS_SYNC",
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequest.Builder(
                        StatusSyncWorker::class.java,
                        Duration.ofMinutes(15),
                        Duration.ofMinutes(5)
                    ).build()
                )
        }
    }
}