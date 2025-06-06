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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.data.LligaCarpeta.TipusLliga
import org.example.project.network.HttpClient

class CreateLeaguePage(val updatePage: (String) -> Unit, val token: String) {
    val page = "CreateLeague"
    private val httpClient = HttpClient()

    @Composable
    fun show() {
        var nomLliga by remember { mutableStateOf("") }
        var tipusLliga by remember { mutableStateOf(TipusLliga.PUBLICA) }
        var contrasenya by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        var isCreatingLeague by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

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
                        text = "Crear Nova Lliga",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    value = nomLliga,
                    onValueChange = { nomLliga = it },
                    label = { Text("Nom de la Lliga", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Gray,
                        textColor = Color.White,
                        cursorColor = Color(0xFFCC0000),
                        focusedLabelColor = Color(0xFFCC0000),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Text(
                    text = "Tipus de Lliga:",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipusLliga == TipusLliga.PUBLICA,
                        onClick = { tipusLliga = TipusLliga.PUBLICA },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFCC0000),
                            unselectedColor = Color.Gray
                        )
                    )
                    Text("Pública", color = Color.White)

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = tipusLliga == TipusLliga.PRIVADA,
                        onClick = { tipusLliga = TipusLliga.PRIVADA },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFCC0000),
                            unselectedColor = Color.Gray
                        )
                    )
                    Text("Privada", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (tipusLliga == TipusLliga.PRIVADA) {
                    TextField(
                        value = contrasenya,
                        onValueChange = { contrasenya = it },
                        label = { Text("Contrasenya de la Lliga", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Gray,
                            textColor = Color.White,
                            cursorColor = Color(0xFFCC0000),
                            focusedLabelColor = Color(0xFFCC0000),
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                }

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                error = ""
                                if (nomLliga.isBlank()) {
                                    error = "El nom de la lliga no pot estar buit"
                                    return@launch
                                }

                                if (tipusLliga == TipusLliga.PRIVADA && contrasenya.isBlank()) {
                                    error = "La contrasenya no pot estar buida per a lligues privades"
                                    return@launch
                                }

                                isCreatingLeague = true
                                val result = httpClient.postCreateLliga(
                                    token = token,
                                    nomLliga = nomLliga,
                                    tipusLliga = tipusLliga.name.uppercase(),
                                    contrasenya = if (tipusLliga == TipusLliga.PRIVADA) contrasenya else null
                                )

                                if (result) {
                                    scaffoldState.snackbarHostState.showSnackbar("Lliga creada exitosament")
                                    delay(1000)
                                    updatePage("Home")
                                } else {
                                    error = "Error al crear la lliga"
                                }

                                isCreatingLeague = false
                            } catch (e: Exception) {
                                error = "Error al crear la lliga: ${e.message}"
                                isCreatingLeague = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                    shape = RoundedCornerShape(4.dp),
                    enabled = !isCreatingLeague
                ) {
                    if (isCreatingLeague) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Crear Lliga", color = Color.White)
                    }
                }
            }
        }
    }
}