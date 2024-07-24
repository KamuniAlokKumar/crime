package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import uk.ac.tees.mad.w9642833.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimeMapScreen(navController: NavHostController) {
    val viewModel: HomeViewModel = viewModel()
    val crimeReports = viewModel.crimeReports.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Crime Map") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                }
            )

        }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            var location = LatLng(0.0, 0.0)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(location, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                crimeReports.value.forEach { crimeReport ->
                    val latLng = parseLatLon(crimeReport.location)
                    Marker(
                        state = rememberMarkerState(position = latLng),
                        title = crimeReport.title,
                        snippet = crimeReport.details,
                        onClick = {
                            true
                        }
                    )

                }
            }

        }
    }
}

fun parseLatLon(input: String): LatLng {
    val parts = input.split(", ")
    val lat = parts[0].substringAfter(": ").toDoubleOrNull() ?: 0.0
    val lon = parts[1].substringAfter(": ").toDoubleOrNull() ?: 0.0
    return LatLng(lat, lon)
}