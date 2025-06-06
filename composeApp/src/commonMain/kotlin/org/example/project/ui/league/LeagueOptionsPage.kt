package org.example.project.ui.league

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

class LeagueOptionsPage(val updatePage: (String) -> Unit, val token: String) {
    val page = "LeagueOptionsPage"

    @Composable
    fun show() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1C))
                .padding(WindowInsets.statusBars.asPaddingValues())
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
                    text = "Opcions de Lliga",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = { updatePage("CreateLeague") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Crear Nova Lliga",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { updatePage("UnirseLliga") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF444444)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Unir-se a una Lliga Existent",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}