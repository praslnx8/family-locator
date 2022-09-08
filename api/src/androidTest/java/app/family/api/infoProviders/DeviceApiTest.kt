package app.family.api.infoProviders

import android.content.Context
import android.media.AudioManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.family.api.apis.DeviceApi
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceApiTest {
    private lateinit var deviceApi: DeviceApi

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        deviceApi =
            DeviceApi(context, context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }

    @Test
    fun shouldReturnBatteryInfo() {
        assertThat(deviceApi.getBatteryPercentage()).isEqualTo(100)
    }

    @Test
    fun shouldReturnAudioInfo() {
        assertThat(deviceApi.isPhoneSilent()).isFalse()
    }
}