package com.fingo.quizjet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Quiz : Screen("quiz")
    object DateQuiz : Screen("date_quiz")
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Quiz.route) {
            QuizScreen(navController)
        }
        composable(Screen.DateQuiz.route) {
            DateQuizScreen(navController)
        }
    }
}

@Composable
fun AppDrawer(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch { drawerState.close() }
            navController.navigate(Screen.Home.route)
        }) {
            Text("Home")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch { drawerState.close() }
            navController.navigate(Screen.Quiz.route)
        }) {
            Text("Math Quiz")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch { drawerState.close() }
            navController.navigate(Screen.DateQuiz.route)
        }) {
            Text("Date Quiz")
        }
    }
}



