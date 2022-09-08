package app.family.api.apis

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.BatteryManager

class DeviceApi(private val context: Context, private val audioManager: AudioManager) {

    fun getBatteryPercentage(): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, iFilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 0

        val batteryRatio = level / scale.toFloat()
        val batteryPct = batteryRatio * 100

        return batteryPct.toInt()
    }

    fun isPhoneSilent(): Boolean {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)

        val volume = (currentVolume * 100) / maxVolume

        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_SILENT -> true
            AudioManager.RINGER_MODE_VIBRATE -> true
            else -> volume < 10
        }
    }
}