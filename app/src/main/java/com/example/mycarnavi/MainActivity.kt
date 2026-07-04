package com.example.mycarnavi

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.mycarnavi.ui.theme.MyCarNaviTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {

    private val viewModel: MapViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                viewModel.updateCurrentLocation(
                    location.latitude,
                    location.longitude
                )
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) {
            viewModel.setLocationPermissionGranted(true)
            startLocationUpdates()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (hasLocationPermission()) {
            viewModel.setLocationPermissionGranted(true)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        enableEdgeToEdge()

        setContent {
            MyCarNaviTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MapScreen(viewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000L
        ).build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {

            when (event.keyCode) {

                KeyEvent.KEYCODE_DPAD_LEFT ->
                    viewModel.moveWest()

                KeyEvent.KEYCODE_DPAD_RIGHT ->
                    viewModel.moveEast()

                KeyEvent.KEYCODE_DPAD_UP ->
                    viewModel.moveNorth()

                KeyEvent.KEYCODE_DPAD_DOWN ->
                    viewModel.moveSouth()

                KeyEvent.KEYCODE_PAGE_UP ->
                    viewModel.zoomIn()

                KeyEvent.KEYCODE_PAGE_DOWN ->
                    viewModel.zoomOut()

                KeyEvent.KEYCODE_ENTER,
                KeyEvent.KEYCODE_DPAD_CENTER ->
                    viewModel.setDestinationToCenter()

                KeyEvent.KEYCODE_SPACE ->
                    viewModel.centerOnCurrentLocation()
            }
        }

        return super.dispatchKeyEvent(event)
    }
}