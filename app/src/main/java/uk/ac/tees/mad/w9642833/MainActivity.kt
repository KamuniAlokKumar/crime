package uk.ac.tees.mad.w9642833

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9642833.screens.CrimeMapScreen
import uk.ac.tees.mad.w9642833.screens.MainScreen
import uk.ac.tees.mad.w9642833.screens.ReportCrimeScreen
import uk.ac.tees.mad.w9642833.screens.SplashScreen
import uk.ac.tees.mad.w9642833.ui.theme.CrimeAlertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CrimeAlertTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") { SplashScreen(navController = navController) }
        composable("main_screen") { MainScreen(navController) }
        composable("report_crime_screen") { ReportCrimeScreen(navController) }
        composable("crime_map_screen") { CrimeMapScreen(navController)}
    }

}