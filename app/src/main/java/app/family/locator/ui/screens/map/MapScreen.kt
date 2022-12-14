package app.family.locator.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.presentation.vms.MapScreenViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber

@Composable
fun MapScreen(viewModel: MapScreenViewModel = hiltViewModel()) {
    val mapDataList = viewModel.getMapData().collectAsState(initial = emptyList())

    val cameraPosition = rememberCameraPositionState {
        val pos = mapDataList.value.firstOrNull()?.position
        position = CameraPosition.fromLatLngZoom(LatLng(pos?.lat ?: 0.0, pos?.lon ?: 0.0), 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition
    ) {
        mapDataList.value.forEach { mapDataState ->
            MapMarker(
                position = LatLng(
                    mapDataState.position.lat,
                    mapDataState.position.lon
                ),
                title = mapDataState.name,
                name = mapDataState.name,
            )
        }
    }

    if (mapDataList.value.size > 1) {
        try {
            val latLngBoundsBuilder = LatLngBounds.builder()
            mapDataList.value.forEach { mapDataState ->
                latLngBoundsBuilder.include(
                    LatLng(
                        mapDataState.position.lat,
                        mapDataState.position.lon
                    )
                )
            }
            cameraPosition.move(
                CameraUpdateFactory.newLatLngBounds(
                    latLngBoundsBuilder.build(),
                    10
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }


}