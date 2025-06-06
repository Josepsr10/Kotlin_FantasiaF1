package org.example.project.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import f1_fantasia.composeapp.generated.resources.Res
import f1_fantasia.composeapp.generated.resources.logo1
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.layout.ContentScale
import org.example.project.network.HttpClient

class RegisterPage(val updatePage: (String) -> Unit) {
    val page = "Register"
    private val httpClient = HttpClient()

    @Composable
    fun show() {
        var nom by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.logo1),
                contentDescription = "Fondo de la página de registro",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "Registrar-se",
                style = MaterialTheme.typography.h4,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 120.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Usuari", color = Color.White) },
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

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contrasenya", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
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

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Gmail", color = Color.White) },
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

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                if (nom.isBlank() || email.isBlank() || password.isBlank()) {
                                    error = "Tots els camps són obligatoris"
                                    return@launch
                                }

                                httpClient.postRegister(
                                    nom = nom,
                                    email = email,
                                    password = password
                                )
                                updatePage("Login")
                            } catch (e: Exception) {
                                error = "Error al registrar-se: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Registrar-se", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { updatePage("Login") }
                ) {
                    Text("Ja tens compte? Inicia sessió!", color = Color.White)
                }
            }
        }
    }
}