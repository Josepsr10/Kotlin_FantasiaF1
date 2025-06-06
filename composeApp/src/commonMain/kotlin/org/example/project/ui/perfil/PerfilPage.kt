package org.example.project.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.data.UsuariInfo
import org.example.project.network.HttpClient

class PerfilPage(val updatePage: (String) -> Unit, val token: String) {
    val page = "PerfilPage"
    private val httpClient = HttpClient()

    @Composable
    fun show() {
        val scaffoldState = rememberScaffoldState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFCC0000))
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        title = { Text("Perfil d'Usuari", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { updatePage("Home") }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Tornar",
                                    tint = Color.White
                                )
                            }
                        },
                        backgroundColor = Color(0xFFCC0000),
                        contentColor = Color.White,
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                content = { paddingValues ->
                    PerfilContent(paddingValues, token, scaffoldState, updatePage)
                },
                backgroundColor = Color(0xFF1C1C1C)
            )
        }
    }

    @Composable
    fun PerfilContent(paddingValues: PaddingValues, token: String, scaffoldState: ScaffoldState, updatePage: (String) -> Unit) {
        var usuarioInfo by remember { mutableStateOf<UsuariInfo?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf("") }

        var nomNou by remember { mutableStateOf("") }
        var contrasenyaActual by remember { mutableStateOf("") }
        var contrasenyaNova by remember { mutableStateOf("") }

        var isEditingName by remember { mutableStateOf(false) }
        var isEditingPassword by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(true) {
            try {
                val response = httpClient.getUsuariInfo(token)
                usuarioInfo = response.usuari
                isLoading = false
            } catch (e: Exception) {
                error = e.message ?: "Error desconegut"
                isLoading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1C1C1C))
                .padding(16.dp)
        ) {
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
            } else if (usuarioInfo != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    backgroundColor = Color(0xFF272727),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFF333333), shape = RoundedCornerShape(30.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Perfil",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Informació d'usuari",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Rol: ${usuarioInfo!!.rol}",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Divider(
                            color = Color(0xFF444444),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Nom d'usuari",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = usuarioInfo!!.nomUsuari,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            TextButton(
                                onClick = { isEditingName = true },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFFCC0000)
                                )
                            ) {
                                Text("Editar")
                            }
                        }

                        Text(
                            text = "Email",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = usuarioInfo!!.correuElectronic,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
                        )

                        Text(
                            text = "Contrasenya",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "••••••••••",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            TextButton(
                                onClick = { isEditingPassword = true },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFFCC0000)
                                )
                            ) {
                                Text("Canviar")
                            }
                        }
                    }
                }

                if (isEditingName) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        backgroundColor = Color(0xFF272727),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Canviar nom d'usuari",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            TextField(
                                value = nomNou,
                                onValueChange = { nomNou = it },
                                label = { Text("Nou nom d'usuari", color = Color.White) },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFF333333),
                                    textColor = Color.White,
                                    cursorColor = Color(0xFFCC0000),
                                    focusedLabelColor = Color(0xFFCC0000),
                                    unfocusedLabelColor = Color.Gray
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        isEditingName = false
                                        nomNou = ""
                                    }
                                ) {
                                    Text("Cancel·lar", color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                if (nomNou.isNotEmpty()) {
                                                    val resposta = httpClient.patchUpdateNom(token, nomNou)
                                                    usuarioInfo = usuarioInfo?.copy(nomUsuari = nomNou)
                                                    isEditingName = false
                                                    nomNou = ""
                                                    scaffoldState.snackbarHostState.showSnackbar(resposta)
                                                } else {
                                                    scaffoldState.snackbarHostState.showSnackbar("El nom no pot estar buit")
                                                }
                                            } catch (e: Exception) {
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    e.message ?: "Error desconegut"
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                                    enabled = nomNou.isNotEmpty()
                                ) {
                                    Text("Guardar", color = Color.White)
                                }
                            }
                        }
                    }
                }

                if (isEditingPassword) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        backgroundColor = Color(0xFF272727),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Canviar contrasenya",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            TextField(
                                value = contrasenyaActual,
                                onValueChange = { contrasenyaActual = it },
                                label = { Text("Contrasenya actual", color = Color.White) },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFF333333),
                                    textColor = Color.White,
                                    cursorColor = Color(0xFFCC0000),
                                    focusedLabelColor = Color(0xFFCC0000),
                                    unfocusedLabelColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            TextField(
                                value = contrasenyaNova,
                                onValueChange = { contrasenyaNova = it },
                                label = { Text("Nova contrasenya", color = Color.White) },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFF333333),
                                    textColor = Color.White,
                                    cursorColor = Color(0xFFCC0000),
                                    focusedLabelColor = Color(0xFFCC0000),
                                    unfocusedLabelColor = Color.Gray
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        isEditingPassword = false
                                        contrasenyaActual = ""
                                        contrasenyaNova = ""
                                    }
                                ) {
                                    Text("Cancel·lar", color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                if (contrasenyaActual.isNotEmpty() && contrasenyaNova.isNotEmpty()) {
                                                    val resposta = httpClient.patchUpdatePassword(
                                                        token,
                                                        contrasenyaActual,
                                                        contrasenyaNova
                                                    )
                                                    isEditingPassword = false
                                                    contrasenyaActual = ""
                                                    contrasenyaNova = ""
                                                    scaffoldState.snackbarHostState.showSnackbar(resposta)
                                                } else {
                                                    scaffoldState.snackbarHostState.showSnackbar("Els camps de contrasenya no poden estar buits")
                                                }
                                            } catch (e: Exception) {
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    e.message ?: "Error desconegut"
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                                    enabled = contrasenyaActual.isNotEmpty() && contrasenyaNova.isNotEmpty()
                                ) {
                                    Text("Guardar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}