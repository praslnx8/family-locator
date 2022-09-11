package app.family.api.apis

import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalityApi(private val geocoder: Geocoder) {

    fun getLocality(lat: Double, lon: Double): Flow<String?> = callbackFlow {
        if (Geocoder.isPresent()) {
            geocoder.getFromLocation(lat, lon, 6)?.let { addresses ->
                addresses.firstOrNull()?.let {
                    Log.i("Locality API", "Fetched Location " + it.locality)
                    trySend(it.locality)
                }
            }
        } else {
            Log.d("Locality API", "Geocoder not present")
            trySend(null)
        }
        awaitClose()
    }
}