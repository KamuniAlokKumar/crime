package uk.ac.tees.mad.w9642833.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.models.CrimeReport
import uk.ac.tees.mad.w9642833.viewmodel.ReportCrimeViewModel
import java.io.ByteArrayOutputStream


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportCrimeScreen(
    navHostController: NavHostController,
    viewModel: ReportCrimeViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var crimeTitle by remember { mutableStateOf("") }
    var crimeDetails by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }
    var mediaUploaded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                byteArray = handleImageSelection(it, context)
            }
            mediaUploaded = uri != null
        }

    val requestCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                byteArray = handleImageCapture(it)
            }
            mediaUploaded = bitmap != null
        }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA) {
        if (it) {
            requestCameraLauncher.launch(null)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Report a Crime") }, navigationIcon = {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) {

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = WindowInsets.ime
            ) {
                // Sheet content
                PhotoPickerOptionBottomSheet(onGalleryClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                    mediaPickerLauncher.launch("image/*")
                }, onCameraClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                    if (!cameraPermission.status.isGranted) {
                        cameraPermission.launchPermissionRequest()
                    }
                    if (cameraPermission.status.isGranted) {
                        requestCameraLauncher.launch(null)
                    }
                })
            }
        }
        Column(Modifier.padding(it)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Enter Crime title", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = crimeTitle,
                    onValueChange = { crimeTitle = it },
                    label = { Text("Crime Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Submit Crime Details", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = crimeDetails,
                    onValueChange = { crimeDetails = it },
                    label = { Text("Crime Details") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (mediaUploaded) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { }
                        ) {
                            Text("Media Added")
                        }
                    } else {
                        OutlinedButton(onClick = { showBottomSheet = true }) {
                            Text("Upload Photo/Video")
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    if (location.isNotEmpty()) {
                        Button(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }) {
                            Text(text = "Location Added")

                        }
                    } else {
                        OutlinedButton(modifier = Modifier.weight(1f), onClick = {
                            if (locationPermissionState.status.isGranted) {
                                fusedLocationClient.lastLocation.addOnSuccessListener { locationResult ->
                                    location =
                                        "Lat: ${locationResult.latitude}, Lon: ${locationResult.longitude}"
                                    Log.d("LOCATION", location)

                                }.addOnFailureListener { ex ->
                                    Log.d("LOCATION ERR", ex.message.toString())
                                }
                            } else {

                                locationPermissionState.launchPermissionRequest()
                            }
                        }) {
                            Text(text = "Tag Location")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.submitCrimeReport(crimeTitle, crimeDetails, location, byteArray)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Report")
                }
                Spacer(modifier = Modifier.height(16.dp))
                viewModel.submissionStatus?.let { status ->
                    Text(if (status) "Report submitted successfully!" else "Failed to submit report.")
                    if (status) {
                        scope.launch(Dispatchers.Main) {
                            delay(3000L)
                            navHostController.navigate("main_screen")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PhotoPickerOptionBottomSheet(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clickable {
                    onCameraClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )

            Text(
                text = "Camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    onGalleryClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Image, contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )
            Text(
                text = "Gallery",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
    }
}

fun handleImageCapture(bitmap: Bitmap): ByteArray {
    return convertBitmapToByteArray(bitmap)
}

fun handleImageSelection(uri: Uri, context: Context): ByteArray {
    val bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media
            .getBitmap(context.contentResolver, uri)

    } else {
        val source = ImageDecoder
            .createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
    return convertBitmapToByteArray(bitmap)
}


fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

