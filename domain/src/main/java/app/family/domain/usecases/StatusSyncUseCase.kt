package app.family.domain.usecases

import app.family.api.apis.LocalityApi
import app.family.api.apis.LocationAPI
import app.family.api.apis.MyStatusApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class StatusSyncUseCase(
    private val locationAPI: LocationAPI,
    private val localityApi: LocalityApi,
    private val myStatusApi: MyStatusApi
) {

    fun listenAndSync(): Flow<Unit> = flow {
        locationAPI.listenToLocationChange().collect { locationDto ->
            val locality = localityApi.getLocality(locationDto.lat, locationDto.lon).first()
            if (locality != null) {
                myStatusApi.updateLocation(
                    locationDto.lat,
                    locationDto.lon,
                    locality,
                    System.currentTimeMillis()
                )
            }
        }

        //Listen to Activity Detection update

    }
}