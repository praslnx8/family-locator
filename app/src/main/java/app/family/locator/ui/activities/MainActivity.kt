package app.family.locator.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.family.locator.services.StatusSyncService
import app.family.locator.services.StatusSyncWorker
import app.family.locator.ui.nav.HomeNavigation
import app.family.locator.ui.theme.FamilyLocatorTheme
import app.family.locator.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUI()
        syncStatus()
        notificationUtils.clearChatNotification()
    }

    private fun attachUI() {
        setContent {
            FamilyLocatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HomeNavigation()
                }
            }
        }
    }

    private fun syncStatus() {
        StatusSyncService.startService(this)
        StatusSyncWorker.startPeriodicWork(this)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}