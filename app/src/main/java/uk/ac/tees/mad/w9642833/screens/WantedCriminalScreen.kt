package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WantedCriminalScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Wanted Criminals")
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

        }

    }
}