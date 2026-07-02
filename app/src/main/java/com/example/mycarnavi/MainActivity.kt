package com.example.mycarnavi

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mycarnavi.ui.theme.MyCarNaviTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            }
        }

        return super.dispatchKeyEvent(event)
    }
}