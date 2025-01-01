package com.fingo.quizjet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlin.random.Random

@Composable
fun QuizAppTheme(navController: NavHostController) {
    val colors = lightColorScheme(
        primary = Color(0xFF81C784), // Muted Green
        onPrimary = Color.White,
        primaryContainer = Color(0xFFA5D6A7), // Light Muted Green
        secondary = Color(0xFF4FC3F7), // Soft Blue
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE1F5FE), // Light Blue
        background = Color(0xFFFFF59D), // Soft Yellow
        onBackground = Color.Black,
        surface = Color(0xFFFFAB91), // Soft Orange
        onSurface = Color.Black,
        error = Color(0xFFF44336), // Soft Red
        onError = Color.White
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
    ) {
        NavigationComponent(navController)
    }
}






@Composable
fun QuizScreen(navController: NavController) {
    var firstNumber by remember { mutableStateOf(0) }
    var secondNumber by remember { mutableStateOf(0) }
    var operation by remember { mutableStateOf("+") }
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    // Initialize quiz question
    LaunchedEffect(Unit) {
        val (num1, num2, op) = generateQuizQuestion()
        firstNumber = num1
        secondNumber = num2
        operation = op
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .background(Color(0xFFFFE082)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Solve the Quiz!", fontSize = 24.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "$firstNumber $operation $secondNumber =", fontSize = 20.sp, color = Color.DarkGray)
                TextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Your Answer") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Show numeric keyboard
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    isCorrect = checkAnswer(firstNumber, secondNumber, operation, userAnswer.toInt())
                }) {
                    Text("Submit")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Display feedback based on the value of isCorrect
                when (isCorrect) {
                    true -> {
                        Text(text = "Correct! ⭐", fontSize = 20.sp, color = Color.Green)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            val (num1, num2, op) = generateQuizQuestion()
                            firstNumber = num1
                            secondNumber = num2
                            operation = op
                            userAnswer = ""
                            isCorrect = null
                        }) {
                            Text("Next Question")
                        }
                    }
                    false -> Text(text = "Try Again!", fontSize = 20.sp, color = Color.Red)
                    else -> Text("")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    navController.navigate(Screen.DateQuiz.route)
                }) {
                    Text("Go to Date Quiz")
                }
            }
        }
    }
}


fun generateQuizQuestion(): Triple<Int, Int, String> {
    val operations = listOf("+", "-", "*")
    val firstNumber = Random.nextInt(1, 11)
    val secondNumber = Random.nextInt(1, 11)
    val operation = operations.random()
    return Triple(firstNumber, secondNumber, operation)
}

fun checkAnswer(firstNumber: Int, secondNumber: Int, operation: String, userAnswer: Int): Boolean {
    return when (operation) {
        "+" -> firstNumber + secondNumber == userAnswer
        "-" -> firstNumber - secondNumber == userAnswer
        "*" -> firstNumber * secondNumber == userAnswer
        else -> false
    }
}


