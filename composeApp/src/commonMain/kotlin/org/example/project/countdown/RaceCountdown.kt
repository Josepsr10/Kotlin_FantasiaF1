package org.example.project.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.datetime.*

class CountdownTimer(private val targetTime: LocalDateTime) {
    fun getRemainingTime(): Triple<Long, Long, Long> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val targetInstant = targetTime.toInstant(TimeZone.currentSystemDefault())
        val nowInstant = now.toInstant(TimeZone.currentSystemDefault())

        val totalSeconds = (targetInstant - nowInstant).inWholeSeconds.coerceAtLeast(0)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return Triple(hours, minutes, seconds)
    }
}

@Composable
fun RaceCountdown(targetDateTime: LocalDateTime) {
    val countdownTimer = remember { CountdownTimer(targetDateTime) }

    var hours by remember { mutableStateOf(0L) }
    var minutes by remember { mutableStateOf(0L) }
    var seconds by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            val (h, m, s) = countdownTimer.getRemainingTime()
            hours = h
            minutes = m
            seconds = s
            delay(1000)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF006400), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text("GRAND PRIX WEEKEND", color = Color.White, fontSize = 14.sp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$hours", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("HRS", color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("$minutes", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("MINS", color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("$seconds", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("SECS", color = Color.White)
        }
    }
}
