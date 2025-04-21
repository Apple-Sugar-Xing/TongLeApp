package com.tongle.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tongle.app.ui.screens.HomeScreen
import com.tongle.app.ui.screens.MusicScreen
import com.tongle.app.ui.screens.ProfileScreen

@Composable
fun TongLeApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "故事") },
                    label = { Text("故事") },
                    selected = currentDestination?.hierarchy?.any { it.route == "stories" } == true,
                    onClick = {
                        navController.navigate("stories") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = "儿歌") },
                    label = { Text("儿歌") },
                    selected = currentDestination?.hierarchy?.any { it.route == "songs" } == true,
                    onClick = {
                        navController.navigate("songs") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                    label = { Text("我的") },
                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "stories",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("stories") { HomeScreen() }
            composable("songs") { MusicScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}
