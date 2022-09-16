package app.family.locator.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import app.family.presentation.vms.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoutingActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndNavigate()
    }

    private fun checkAndNavigate() {
        val context = this
        lifecycleScope.launch() {
            if (checkPermissionsGranted() && splashViewModel.checkNavigateToHome().first()) {
                MainActivity.startActivity(context)
                finish()
            } else {
                LoginActivity.startActivity(context)
                finish()
            }
        }

    }

    private fun checkPermissionsGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
    }
}