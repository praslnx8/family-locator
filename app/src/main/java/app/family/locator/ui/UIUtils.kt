package app.family.locator.ui

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import app.family.domain.models.status.WeatherType
import app.family.domain.models.status.WeatherType.CLEAR
import app.family.domain.models.status.WeatherType.CLOUDY
import app.family.domain.models.status.WeatherType.FOGGY
import app.family.domain.models.status.WeatherType.HAZY
import app.family.domain.models.status.WeatherType.ICY
import app.family.domain.models.status.WeatherType.RAINY
import app.family.domain.models.status.WeatherType.SNOWY
import app.family.domain.models.status.WeatherType.STORMY
import app.family.domain.models.status.WeatherType.WINDY
import app.family.locator.R
import java.util.concurrent.TimeUnit

object UIUtils {

    @DrawableRes
    fun getWeatherIcon(weatherType: WeatherType, isNight: Boolean): Int {
        return when (weatherType) {
            CLEAR -> if (isNight) R.drawable.ic_clear_night else R.drawable.ic_clear_day
            WINDY -> if (isNight) R.drawable.ic_clear_night else R.drawable.ic_clear_day
            STORMY -> if (isNight) R.drawable.ic_stormy else R.drawable.ic_stormy
            RAINY -> if (isNight) R.drawable.ic_rainy else R.drawable.ic_rainy
            SNOWY -> if (isNight) R.drawable.ic_snowy else R.drawable.ic_snowy
            ICY -> if (isNight) R.drawable.ic_icy else R.drawable.ic_icy
            HAZY -> if (isNight) R.drawable.ic_haze else R.drawable.ic_haze
            FOGGY -> if (isNight) R.drawable.ic_foggy_night else R.drawable.ic_foggy_day
            CLOUDY -> if (isNight) R.drawable.ic_cloudy_night else R.drawable.ic_cloudy_day
        }
    }

    fun getRelativeTime(time: Long): String {
        if (System.currentTimeMillis() - time < TimeUnit.MINUTES.toMillis(2)) {
            return "Now"
        }
        return DateUtils.getRelativeTimeSpanString(time)
            .toString()
    }

    fun getAvatarText(name: String): String {
        val stringBuilder = StringBuilder()
        name.split(" ").forEach {
            stringBuilder.append(it.firstOrNull() ?: "")
        }
        return stringBuilder.toString()
    }
}