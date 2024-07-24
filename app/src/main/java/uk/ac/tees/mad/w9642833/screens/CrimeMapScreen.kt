package uk.ac.tees.mad.w9642833.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import uk.ac.tees.mad.w9642833.models.CrimeReport
import uk.ac.tees.mad.w9642833.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimeMapScreen(navController: NavHostController) {
    val viewModel: HomeViewModel = viewModel()
    val crimeReports = viewModel.crimeReports.collectAsState()
    var selectedCrimeReport by remember { mutableStateOf<CrimeReport?>(null) }

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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (crimeReports.value.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                ) {
                    Text(text = "No crime reports found.")
                }
            }
            var location = LatLng(0.0, 0.0)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(location, 10f)
            }
            LaunchedEffect(crimeReports.value) {
                if (crimeReports.value.isNotEmpty()) {
                    location = parseLatLon(crimeReports.value[0].location)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(location, 10f),
                        2000
                    )
                }
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                crimeReports.value.forEach { crimeReport ->
                    val latLng = parseLatLon(crimeReport.location)
                    Marker(
                        state = rememberMarkerState(position = latLng),
                        onClick = {
                            selectedCrimeReport = crimeReport
                            true
                        },
                    )
                }

            }
            selectedCrimeReport?.let { crime ->
                Box(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .align(Alignment.TopEnd)
                            .clickable {
                                selectedCrimeReport = null
                            }
                            .zIndex(3f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(4f)
                                .padding(8.dp)
                        )
                    }
                    CrimeReportItem(report = crime)
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
