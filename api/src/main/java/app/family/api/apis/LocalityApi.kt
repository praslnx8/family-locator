package app.family.api.apis

import android.location.Geocoder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalityApi(private val geocoder: Geocoder) {

    fun getLocality(lat: Double, lon: Double): Flow<String?> = callbackFlow {
        if (Geocoder.isPresent()) {
            geocoder.getFromLocation(lat, lon, 6)?.let { addresses ->
                val locality = addresses.firstOrNull()?.locality
                trySend(locality)
                close()
            }
        } else {
            close()
        }
        awaitClose()
    }
}