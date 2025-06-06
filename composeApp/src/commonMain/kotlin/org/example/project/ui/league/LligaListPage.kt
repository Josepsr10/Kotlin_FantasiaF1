package org.example.project.ui.league

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.LligaCarpeta.Lliga
import org.example.project.data.LligaCarpeta.TipusLliga
import org.example.project.network.HttpClient
class LligaListPage(val updatePage: (String) -> Unit, val token: String, val navigateToJoinLliga: (Lliga) -> Unit) {
    private val httpClient = HttpClient()
    val page = "LligaList"

    @Composable
    fun show() {
        var lligues by remember { mutableStateOf<List<Lliga>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf("") }

        val scaffoldState = rememberScaffoldState()

        LaunchedEffect(key1 = true) {
            try {
                val response = httpClient.getLligues(token)
                if (response.ok) {
                    lligues = response.lligues
                } else {
                    error = "No s'han pogut carregar les lligues"
                }
            } catch (e: Exception) {
                error = e.message ?: "Error desconegut"
            } finally {
                isLoading = false
            }
        }

        Scaffold(scaffoldState = scaffoldState) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1C1C1C))
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { updatePage("Home") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tornar",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Lligues Disponibles",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFCC0000))
                    }
                } else if (error.isNotEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                } else if (lligues.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hi ha lligues disponibles",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(lligues) { lliga ->
                            LligaItem(lliga) {
                                navigateToJoinLliga(lliga)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LligaItem(lliga: Lliga, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color(0xFF2C2C2C),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = lliga.nomLliga,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val tipusColor = if (lliga.tipusLliga == TipusLliga.PUBLICA)
                        Color(0xFF4CAF50) else Color(0xFFE91E63)

                    Text(
                        text = lliga.tipusLliga.name,
                        color = tipusColor,
                        fontSize = 14.sp
                    )
                }

                Chip(
                    tipusLliga = lliga.tipusLliga
                )
            }
        }
    }

    @Composable
    private fun Chip(tipusLliga: TipusLliga) {
        val (color, text) = when (tipusLliga) {
            TipusLliga.PUBLICA -> Pair(Color(0xFF4CAF50), "Pública")
            TipusLliga.PRIVADA -> Pair(Color(0xFFE91E63), "Privada")
        }

        Surface(
            modifier = Modifier
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
            color = color.copy(alpha = 0.2f)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}