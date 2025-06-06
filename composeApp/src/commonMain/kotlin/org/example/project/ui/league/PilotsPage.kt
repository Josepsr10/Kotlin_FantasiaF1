package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.LligaCarpeta.Lliga
import org.example.project.data.PilotCarpeta.Pilot
import org.example.project.data.PilotCarpeta.PilotsInfoResponse
import org.example.project.network.HttpClient

class PilotsPage(
    val updatePage: (String) -> Unit,
    val token: String,
    val lliga: Lliga
) {
    val page = "PilotsPage"

    @Composable
    fun show() {
        val httpClient = HttpClient()
        var pilotsResponse by remember { mutableStateOf<PilotsInfoResponse?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf("") }

        LaunchedEffect(lliga.lligaId) {
            try {
                println("cargant pilots per lliga: ${lliga.lligaId}")
                val resposta = httpClient.getPilotsInfo(token, lliga.lligaId)
                pilotsResponse = resposta
                isLoading = false
                println("Pilots del usauri cargats: ${resposta.data.pilots.size}")
            } catch (e: Exception) {
                println("Error cargant pilots: ${e.message}")
                error = "Error carregant els teus pilots: ${e.message}"
                isLoading = false
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Els meus pilots - ${lliga.nomLliga}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { updatePage("Home") }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Tornar"
                            )
                        }
                    },
                    backgroundColor = Color(0xFFCC0000),
                    contentColor = Color.White,
                    modifier = Modifier.statusBarsPadding()
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFF1C1C1C))
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color(0xFFCC0000)
                            )
                        }
                        error.isNotEmpty() -> {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Button(
                                    onClick = { updatePage("Home") },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000))
                                ) {
                                    Text("Tornar a l'inici", color = Color.White)
                                }
                            }
                        }
                        pilotsResponse == null || pilotsResponse?.data?.pilots.isNullOrEmpty() -> {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No tens pilots assignats en aquesta lliga",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Button(
                                    onClick = { updatePage("Home") },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000))
                                ) {
                                    Text("Tornar a l'inici", color = Color.White)
                                }
                            }
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = "Els teus pilots",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Lliga: ${lliga.nomLliga}",
                                        color = Color.LightGray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }

                                items(pilotsResponse?.data?.pilots ?: emptyList()) { pilot ->
                                    PilotCard(pilot = pilot)
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun PilotCard(pilot: Pilot) {
        Card(
            backgroundColor = Color(0xFF2C2C2C),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            elevation = 6.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color(0xFFCC0000), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Pilot",
                        tint = Color.White,
                        modifier = Modifier.size(45.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = pilot.nom,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Equip: ${pilot.equip}",
                        color = Color.LightGray,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        Text(
                            text = "Nacionalitat: ",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = pilot.nacionalitat,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        Text(
                            text = "Edat: ",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${pilot.edat} anys",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Card(
                        backgroundColor = Color(0xFFFFD700),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${pilot.punts}",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "pts",
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}