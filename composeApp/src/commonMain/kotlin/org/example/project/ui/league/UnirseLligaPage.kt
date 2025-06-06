package org.example.project.ui.league

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.data.LligaCarpeta.Lliga
import org.example.project.data.LligaCarpeta.TipusLliga
import org.example.project.network.HttpClient
class UnirseLligaPage(val updatePage: (String) -> Unit, val token: String) {
    private val httpClient = HttpClient()
    val page = "UnirseLliga"

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
                        title = { Text("Unir-se a una Lliga", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
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
                    LliguesContent(paddingValues, token, scaffoldState, updatePage)
                },
                backgroundColor = Color(0xFF1C1C1C)
            )
        }
    }

    @Composable
    fun LliguesContent(paddingValues: PaddingValues, token: String, scaffoldState: ScaffoldState, updatePage: (String) -> Unit) {
        var lligues by remember { mutableStateOf<List<Lliga>>(emptyList()) }
        var error by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }
        var isJoining by remember { mutableStateOf(false) }
        var selectedLliga by remember { mutableStateOf<Lliga?>(null) }
        var showPasswordDialog by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(true) {
            try {
                val resposta = httpClient.getLligues(token)
                lligues = resposta.lligues
                isLoading = false
                println("Lligues carregades: ${lligues.size}")
            } catch (e: Exception) {
                println("Error al carregar lligues: ${e.message}")
                error = "Error carregant lligues: ${e.message}"
                isLoading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1C1C1C))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
            } else {
                Text(
                    text = "Lligues disponibles",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 16.dp)
                )

                if (lligues.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hi ha lligues disponibles",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    lligues.forEach { lliga ->
                        Card(
                            backgroundColor = Color(0xFF272727),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (lliga.tipusLliga == TipusLliga.PRIVADA)
                                            Icons.Default.Lock else Icons.Default.Lock,
                                        contentDescription = if (lliga.tipusLliga == TipusLliga.PRIVADA)
                                            "Lliga Privada" else "Lliga Pública",
                                        tint = if (lliga.tipusLliga == TipusLliga.PRIVADA)
                                            Color.Yellow else Color.Green,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = lliga.nomLliga,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = if (lliga.tipusLliga == TipusLliga.PRIVADA)
                                                "Privada (Requereix contrasenya)" else "Pública",
                                            color = Color.LightGray,
                                            fontSize = 14.sp
                                        )
                                    }   
                                }

                                Button(
                                    onClick = {
                                        if (lliga.tipusLliga == TipusLliga.PRIVADA) {
                                            selectedLliga = lliga
                                            showPasswordDialog = true
                                        } else {
                                            coroutineScope.launch {
                                                try {
                                                    isJoining = true
                                                    error = ""

                                                    val success = httpClient.unirALliga(
                                                        token = token,
                                                        lligaId = lliga.lligaId,
                                                        contrasenya = null
                                                    )

                                                    if (success) {
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            message = "T'has unit correctament a la lliga.",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                        delay(1000)
                                                        updatePage("Home")
                                                    } else {
                                                        error = "Error en unir-se a la lliga"
                                                    }
                                                } catch (e: Exception) {
                                                    error = e.message ?: "Error desconegut"
                                                } finally {
                                                    isJoining = false
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                                    shape = RoundedCornerShape(4.dp),
                                    enabled = !isJoining
                                ) {
                                    if (isJoining) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Text("Unir-se", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showPasswordDialog && selectedLliga != null) {
                PasswordDialog(
                    lligaNom = selectedLliga!!.nomLliga,
                    onDismiss = {
                        showPasswordDialog = false
                    },
                    onConfirm = { enteredPassword ->
                        showPasswordDialog = false
                        coroutineScope.launch {
                            try {
                                isJoining = true
                                error = ""

                                val success = httpClient.unirALliga(
                                    token = token,
                                    lligaId = selectedLliga!!.lligaId,
                                    contrasenya = enteredPassword
                                )

                                if (success) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "T'has unit correctament a la lliga.",
                                        duration = SnackbarDuration.Short
                                    )
                                    delay(1000)
                                    updatePage("Home")
                                } else {
                                    error = "Error en unir-se a la lliga"
                                }
                            } catch (e: Exception) {
                                error = e.message ?: "Error desconegut"
                            } finally {
                                isJoining = false
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun PasswordDialog(
        lligaNom: String,
        onDismiss: () -> Unit,
        onConfirm: (String) -> Unit
    ) {
        var passwordInput by remember { mutableStateOf("") }

        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                backgroundColor = Color(0xFF2C2C2C),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Unir-se a $lligaNom",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Aquesta lliga és privada. Introdueix la contrasenya per unir-te:",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Contrasenya", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Gray,
                            textColor = Color.White,
                            cursorColor = Color(0xFFCC0000),
                            focusedLabelColor = Color(0xFFCC0000),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel·lar", color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { onConfirm(passwordInput) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000))
                        ) {
                            Text("Unir-se", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}