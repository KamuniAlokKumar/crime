package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.models.CrimeReport
import uk.ac.tees.mad.w9642833.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

        items(crimeReports) { report ->
            Text(text = "${report.title}")
        }
    }
}


@Composable
fun DrawerContent(navController: NavHostController) {
    Column(Modifier.fillMaxWidth()) {
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
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            label = { Text(text = "Wanted Criminals") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            label = { Text(text = "Criminal Background Check") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            label = { Text(text = "Report a Crime") },
            selected = false,
            onClick = { navController.navigate("report_crime_screen") }
        )
    }
}