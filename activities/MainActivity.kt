package com.example.projetmobile.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetmobile.viewModel.BookViewModel
import com.example.projetmobile.view.activities.BookActivity
import com.example.projetmobile.view.activities.BookRequestScreen
import com.example.projetmobile.view.navigation.AllDestinations
import com.example.projetmobile.view.navigation.AppNavigationActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val bookViewModel: BookViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
                SampleAppNavGraph(bookViewModel = bookViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SampleAppNavGraph(
    bookViewModel : BookViewModel,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME
    val navigationActions = remember(navController) { AppNavigationActions(navController) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Book App", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Home Page") },
                    selected = false,
                    onClick = {  navigationActions.navigateToHome()}
                )
                NavigationDrawerItem(
                    label = { Text(text = "Search by Category") },
                    selected = false,
                    onClick = {  navigationActions.navigateToSearchByCategory() }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Request a Book") },
                    selected = false,
                    onClick = {  navigationActions.navigateToRequestABook() }
                )

            }}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = currentRoute) },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = { IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }, content = {
                        Icon(
                            imageVector = Icons.Default.Menu, contentDescription = null
                        )
                    })
                    }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer))
            }, modifier = Modifier
        ) {
            MyAppNavHost(bookViewModel=bookViewModel, navController = navController)
        }
    }
}
@Composable
fun MyAppNavHost(
    navController: NavHostController,
    startDestination: String = "Home",
    bookViewModel: BookViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Home") {
            HomePage(bookViewModel = bookViewModel,navController)

        }
        composable("By Category") {
            BookActivity(bookViewModel = bookViewModel)
        }

        composable("Request a missing book") {
            BookRequestScreen()
        }
    }
}