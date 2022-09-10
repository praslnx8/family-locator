package app.family.locator

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.family.locator.services.ActivityTransitionReceiver
import app.family.locator.services.StatusSyncService
import app.family.locator.services.StatusSyncWorker
import app.family.locator.ui.nav.HomeNavigation
import app.family.locator.ui.theme.FamilyLocatorTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUI()
        syncStatus()
    }

    private fun attachUI() {
        setContent {
            FamilyLocatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeNavigation {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            1
                        )
                    }
                }
            }
        }
    }

    private fun syncStatus() {
        ActivityTransitionReceiver.requestForActivityDetection(this)
        StatusSyncService.startService(this)
        StatusSyncWorker.startPeriodicWork(this)
    }
}