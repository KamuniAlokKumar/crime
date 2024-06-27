package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->

        Column(Modifier.padding(innerPadding)) {
            Text(text = "Main")
        }
    }
}