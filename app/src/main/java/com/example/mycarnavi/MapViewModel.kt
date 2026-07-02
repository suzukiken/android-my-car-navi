package com.example.mycarnavi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {

    private val _camera = MutableStateFlow(
        CameraState(
            latitude = 35.681236,
            longitude = 139.767125,
            zoom = 15f
        )
    )

    val camera: StateFlow<CameraState> = _camera

    fun moveWest() {
        move(-100.0, 0.0)
    }

    fun moveEast() {
        move(100.0, 0.0)
    }

    fun moveNorth() {
        move(0.0, 100.0)
    }

    fun moveSouth() {
        move(0.0, -100.0)
    }

    fun zoomIn() {
        _camera.value = _camera.value.copy(
            zoom = (_camera.value.zoom + 1f).coerceAtMost(21f)
        )
    }

    fun zoomOut() {
        _camera.value = _camera.value.copy(
            zoom = (_camera.value.zoom - 1f).coerceAtLeast(3f)
        )
    }

    private fun move(eastMeters: Double, northMeters: Double) {

        val camera = _camera.value

        val dLat = northMeters / 111320.0

        val dLon = eastMeters /
                (111320.0 * kotlin.math.cos(Math.toRadians(camera.latitude)))

        _camera.value = camera.copy(
            latitude = camera.latitude + dLat,
            longitude = camera.longitude + dLon
        )
    }
}