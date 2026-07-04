package com.example.mycarnavi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    viewModel: MapViewModel
) {
    val camera by viewModel.camera.collectAsState()
    val destination by viewModel.destination.collectAsState()

    var mapLoaded by remember { mutableStateOf(false) }

    val destinationMarkerState = remember { MarkerState() }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(camera.latitude, camera.longitude),
            camera.zoom
        )
    }

    LaunchedEffect(camera, mapLoaded) {
        if (!mapLoaded) return@LaunchedEffect

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(camera.latitude, camera.longitude),
                camera.zoom
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                mapLoaded = true
            }
        ) {
            destination?.let { dest ->
                destinationMarkerState.position =
                    LatLng(dest.latitude, dest.longitude)

                Marker(
                    state = destinationMarkerState,
                    title = "目的地"
                )
            }
        }

        Text(
            text = "Zoom ${camera.zoom}\nLat ${camera.latitude}\nLng ${camera.longitude}" +
                    (destination?.let { "\nDest ${it.latitude}, ${it.longitude}" } ?: "")
        )
    }
}