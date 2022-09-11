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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.time.Duration

class StatusSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncUseCase: MyStatusSyncUseCase,
    private val uploadUseCase: UploadStatusUseCase
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        syncUseCase.syncNow().collect()
        val isUploaded = uploadUseCase.uploadMyStatus().first()
        return if (isUploaded) {
            Result.success()
        } else {
            Result.retry()
        }
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