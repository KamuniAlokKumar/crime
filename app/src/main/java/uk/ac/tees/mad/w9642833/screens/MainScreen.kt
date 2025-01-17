package uk.ac.tees.mad.w9642833.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.models.CrimeReport
import uk.ac.tees.mad.w9642833.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Crime Alert", modifier = Modifier.padding(16.dp))
                Divider()
                DrawerContent(navController)
            }
        }) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Crime Alert") }, navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "drawer")
                }
            })
        }) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {

                HomeScreen()

            }
        }
    }


}

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val crimeReports by viewModel.crimeReports.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            if (crimeReports.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Loading or No data found")
                }
            }

        }
        items(crimeReports) { report ->
            CrimeReportItem(report)
        }
    }
}

@Composable
fun CrimeReportItem(report: CrimeReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (report.imageUrl.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Image Available")
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(report.imageUrl),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${report.title}", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Description: ${report.details}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Location: ${report.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun DrawerContent(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NavigationDrawerItem(
            label = { Text(text = "Home") },
            selected = navController.currentBackStackEntry?.destination?.route == "main_screen",
            onClick = {
                navController.navigate("home_screen")
            }
        )
        NavigationDrawerItem(
            label = { Text(text = "Crime Map") },
            selected = false,
            onClick = {
                navController.navigate("crime_map_screen")
            }
        )
        NavigationDrawerItem(
            label = { Text(text = "Wanted Criminals") },
            selected = false,
            onClick = {
                navController.navigate("wanted_criminal_screen")
            }
        )
        NavigationDrawerItem(
            label = { Text(text = "Report a Crime") },
            selected = false,
            onClick = { navController.navigate("report_crime_screen") }
        )
        Spacer(modifier = Modifier.weight(1f))
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Log out",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            selected = true,
            onClick = {
                Firebase.auth.signOut()
                navController.navigate("login_screen") {
                    popUpTo("login_screen") { inclusive = true }
                }
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                selectedTextColor = MaterialTheme.colorScheme.onErrorContainer
            )
        )
    }
}