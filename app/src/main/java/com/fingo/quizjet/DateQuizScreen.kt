package com.fingo.quizjet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun generateRandomDate(startDate: LocalDate, endDate: LocalDate): LocalDate {
    val startEpochDay = startDate.toEpochDay()
    val endEpochDay = endDate.toEpochDay()
    val randomEpochDay = Random.nextLong(startEpochDay, endEpochDay)
    return LocalDate.ofEpochDay(randomEpochDay)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateQuizScreen(navController: NavController) {
    var dateString by remember { mutableStateOf("") }
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    // Generate a random date when the screen is first composed
    LaunchedEffect(Unit) {
        val randomDate = generateRandomDate(LocalDate.of(2020, 1, 1), LocalDate.of(2030, 12, 31))
        dateString = randomDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
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
                Text(text = "What day is it?", fontSize = 24.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = dateString, fontSize = 20.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = userAnswer,
                        onValueChange = { userAnswer = it },
                        label = { Text("Select a Day") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        daysOfWeek.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    userAnswer = day
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    isCorrect = checkDateAnswer(dateString, userAnswer)
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
                            val randomDate = generateRandomDate(LocalDate.of(2020, 1, 1), LocalDate.of(2030, 12, 31))
                            dateString = randomDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            userAnswer = ""
                            isCorrect = null
                        }) {
                            Text("Next Date Question")
                        }
                    }
                    false -> Text(text = "Try Again!", fontSize = 20.sp, color = Color.Red)
                    else -> Text("") // Handle initial state when isCorrect is null
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    navController.navigate(Screen.Quiz.route)
                }) {
                    Text("Back to Math Quiz")
                }
            }
        }
    }
}


fun checkDateAnswer(dateString: String, userAnswer: String): Boolean {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = LocalDate.parse(dateString, dateFormatter)
    val correctDay = date.dayOfWeek.toString().lowercase()
    return userAnswer.lowercase() == correctDay
}
