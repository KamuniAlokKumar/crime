package uk.ac.tees.mad.w9642833.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Crime Alert", modifier = Modifier.padding(16.dp))
                Divider()
                DrawerContent()
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
                Text(text = "Home Screen")
            }
        }
    }


}


@Composable
fun DrawerContent() {
    Column(Modifier.fillMaxWidth()) {
        NavigationDrawerItem(
            label = { Text(text = "Home") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            label = { Text(text = "Crime Map") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            label = { Text(text = "Notifications") },
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
            onClick = { /*TODO*/ }
        )
    }
}