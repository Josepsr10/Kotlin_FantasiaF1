package org.example.project.ui.login

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
import org.example.project.network.HttpClient
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.layout.ContentScale

class LoginPage(val updatePage: (String) -> Unit, val updateToken: (String) -> Unit) {
    val page = "Login"
    private val httpClient = HttpClient()

    @Composable
    fun show() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.logo1),
                contentDescription = "Fondo de la página de login",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "Iniciar Sessió",
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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White) },
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

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                if (email.isBlank() || password.isBlank()) {
                                    error = "Els camps no poden estar buits"
                                    return@launch
                                }

                                val token = httpClient.postLogin(
                                    email = email,
                                    password = password
                                )
                                updateToken(token)
                                updatePage("Home")
                            } catch (e: Exception) {
                                error = "Error al Iniciar Sessió: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Iniciar Sessió", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { updatePage("Register") }
                ) {
                    Text("No tens compte? Registra't!", color = Color.White)
                }
            }
        }
    }
}