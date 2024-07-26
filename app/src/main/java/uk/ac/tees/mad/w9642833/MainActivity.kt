package uk.ac.tees.mad.w9642833

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9642833.screens.CrimeMapScreen
import uk.ac.tees.mad.w9642833.screens.MainScreen
import uk.ac.tees.mad.w9642833.screens.ReportCrimeScreen
import uk.ac.tees.mad.w9642833.screens.SplashScreen
import uk.ac.tees.mad.w9642833.screens.WantedCriminalScreen
import uk.ac.tees.mad.w9642833.ui.theme.CrimeAlertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CrimeAlertTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") { SplashScreen(navController = navController) }
        composable("main_screen") { MainScreen(navController) }
        composable("report_crime_screen") { ReportCrimeScreen(navController) }
        composable("crime_map_screen") { CrimeMapScreen(navController) }
        composable("wanted_criminal_screen") { WantedCriminalScreen(navController) }
    }

}