package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.w9642833.models.Criminal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WantedCriminalScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filteredCriminals = criminals.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Wanted Criminals")
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(filteredCriminals) { criminal ->
                        Text(text = "${criminal.name}")
                    }
                }
            }
        }

    }
}

val criminals = listOf(
    Criminal(1, "John Doe", "Wanted for armed robbery", "https://images.pexels.com/photos/39866/entrepreneur-startup-start-up-man-39866.jpeg"),
    Criminal(2, "Jane Smith", "Wanted for fraud", "https://images.pexels.com/photos/582039/pexels-photo-582039.jpeg"),
    Criminal(3, "Robert Johnson", "Wanted for murder", "https://images.pexels.com/photos/845457/pexels-photo-845457.jpeg"),
    Criminal(4, "Emily Davis", "Wanted for drug trafficking", "https://images.pexels.com/photos/415263/pexels-photo-415263.jpeg"),
    Criminal(5, "Michael Brown", "Wanted for kidnapping", "https://images.pexels.com/photos/819530/pexels-photo-819530.jpeg"),
    Criminal(6, "Sarah Wilson", "Wanted for burglary", "https://images.pexels.com/photos/813940/pexels-photo-813940.jpeg"),
    Criminal(7, "David Martinez", "Wanted for assault", "https://images.pexels.com/photos/886285/pexels-photo-886285.jpeg"),
    Criminal(8, "Laura Garcia", "Wanted for arson", "https://images.pexels.com/photos/762084/pexels-photo-762084.jpeg"),
    Criminal(9, "James Rodriguez", "Wanted for human trafficking", "https://images.pexels.com/photos/713520/pexels-photo-713520.jpeg"),
    Criminal(10, "Linda Hernandez", "Wanted for money laundering", "https://images.pexels.com/photos/1729931/pexels-photo-1729931.jpeg"),
    Criminal(11, "Charles Lee", "Wanted for cybercrime", "https://images.pexels.com/photos/2058659/pexels-photo-2058659.jpeg"),
    Criminal(13, "Christopher Hall", "Wanted for terrorism", "https://images.pexels.com/photos/428340/pexels-photo-428340.jpeg"),
    Criminal(14, "Barbara Allen", "Wanted for tax evasion", "https://example.com/photo14.jpg"),

)
