package app.family.locator.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import app.family.locator.ui.nav.LoginNavigation
import app.family.locator.ui.theme.FamilyLocatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUI()
    }

    private fun attachUI() {
        setContent {
            FamilyLocatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginNavigation {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            BG_LOC_PERMISSION_REQ_CODE
                        )
                        checkAndNavigate()
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkAndNavigate()
    }

    private fun checkAndNavigate() {
        if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MainActivity.startActivity(this)
            finish()
        }
    }

    companion object {
        private const val BG_LOC_PERMISSION_REQ_CODE = 1
    }
}