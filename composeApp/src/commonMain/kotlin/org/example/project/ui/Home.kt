package org.example.project.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import f1_fantasia.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import f1_fantasia.composeapp.generated.resources.spain2
import kotlinx.datetime.LocalDateTime
import org.example.project.countdown.RaceCountdown
import org.example.project.data.LligaCarpeta.Lliga
import org.example.project.data.LligaCarpeta.TipusLliga
import org.example.project.network.HttpClient
import androidx.compose.material.icons.filled.Info
import kotlinx.coroutines.delay

class HomePage(
    val updatePage: (String) -> Unit,
    val token: String,
    val navigateToLligaDetail: (Lliga) -> Unit,
    val navigateToPilots: (Lliga) -> Unit
) {
    val page = "Home"

    @Composable
    fun show() {
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFCC0000))
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        title = { Text("Fantasia F1", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    if (scaffoldState.drawerState.isClosed) {
                                        scaffoldState.drawerState.open()
                                    } else {
                                        scaffoldState.drawerState.close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        backgroundColor = Color(0xFFCC0000),
                        contentColor = Color.White,
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                drawerContent = {
                    DrawerContent(updatePage)
                },
                content = { paddingValues ->
                    HomeContent(paddingValues, token, navigateToLligaDetail)
                },
                backgroundColor = Color(0xFF1C1C1C)
            )
        }
    }

    @Composable
    fun DrawerContent(updatePage: (String) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF1C1C1C))
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }

            Divider(color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            DrawerMenuItem(
                icon = Icons.Default.List,
                title = "Les meves lligues",
                onClick = {  }
            )

            DrawerMenuItem(
                icon = Icons.Default.Person,
                title = "Perfil",
                onClick = { updatePage("PerfilPage") }
            )
            DrawerMenuItem(
                icon = Icons.Default.Info,
                title = "Regles",
                onClick = { updatePage("Regles") }
            )
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { updatePage("Login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tancar sessió", color = Color.White)
            }
        }
    }

    @Composable
    fun DrawerMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFCC0000), shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    @Composable

    fun HomeContent(
        paddingValues: PaddingValues,
        token: String,
        navigateToLligaDetail: (Lliga) -> Unit
    ) {
        val httpClient = HttpClient()
        var lliguesCreades by remember { mutableStateOf<List<Lliga>>(emptyList()) }
        var lliguesParticipacio by remember { mutableStateOf<List<Lliga>>(emptyList()) }
        var errorCreades by remember { mutableStateOf("") }
        var errorParticipacio by remember { mutableStateOf("") }

        var isLeavingLliga by remember { mutableStateOf(false) }
        var leaveMessage by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        var filtreSeleccionat by remember { mutableStateOf("TOTES") }
        var expanded by remember { mutableStateOf(false) }
        val opcions = listOf("TOTES", "PUBLIQUES", "PRIVADES")
        val textFiltre = when (filtreSeleccionat) {
            "PUBLIQUES" -> "Públiques"
            "PRIVADES" -> "Privades"
            else -> "Totes"
        }

        val scrollState = rememberScrollState()

        LaunchedEffect(true) {
            try {
                val resposta = httpClient.getLliguesUsuari(token)
                lliguesCreades = resposta.lligues
                println("Lligues creades: ${lliguesCreades.size}")
            } catch (e: Exception) {
                println(e.message)
                errorCreades = "Error carregant lligues creades: ${e.message}"
            }
        }

        LaunchedEffect(true) {
            try {
                val resposta = httpClient.getLliguesParticipacio(token)
                lliguesParticipacio = resposta.lligues
                println("Lligues on participo: ${lliguesParticipacio.size}")
            } catch (e: Exception) {
                println(e.message)
                errorParticipacio = "Error carregant lligues on participo: ${e.message}"
            }
        }

        val lliguesCreadesFiltered = when (filtreSeleccionat) {
            "PUBLIQUES" -> lliguesCreades.filter { it.tipusLliga == TipusLliga.PUBLICA }
            "PRIVADES" -> lliguesCreades.filter { it.tipusLliga == TipusLliga.PRIVADA }
            else -> lliguesCreades
        }

        val lliguesParticipacioFiltered = when (filtreSeleccionat) {
            "PUBLIQUES" -> lliguesParticipacio.filter { it.tipusLliga == TipusLliga.PUBLICA }
            "PRIVADES" -> lliguesParticipacio.filter { it.tipusLliga == TipusLliga.PRIVADA }
            else -> lliguesParticipacio
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1C1C1C))
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BARCELONA-CATALUNYA 2025",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.spain2),
                    contentDescription = "Circuit de Monaco",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            RaceCountdown(
                targetDateTime = LocalDateTime(2025, 6, 1, 15, 0, 0) // 1 juny 2025, 15:00h
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { updatePage("LeagueOptionsPage") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000))
            ) {
                Text("Crear Lliga", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Filtrar lligues per tipus:",
                    color = Color.White,
                    fontSize = 16.sp
                )

                Box {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = when (filtreSeleccionat) {
                                "PUBLIQUES" -> Color(0xFF666666)
                                "PRIVADES" -> Color(0xFF666666)
                                else -> Color(0xFF666666)
                            }
                        ),
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text(textFiltre, color = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF333333))
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                filtreSeleccionat = "TOTES"
                                expanded = false
                            }
                        ) {
                            Text("Totes", color = Color.White)
                        }

                        DropdownMenuItem(
                            onClick = {
                                filtreSeleccionat = "PUBLIQUES"
                                expanded = false
                            }
                        ) {
                            Text("Públiques", color = Color.White)
                        }

                        DropdownMenuItem(
                            onClick = {
                                filtreSeleccionat = "PRIVADES"
                                expanded = false
                            }
                        ) {
                            Text("Privades", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (leaveMessage.isNotEmpty()) {
                Card(
                    backgroundColor = if (leaveMessage.contains("Error")) Color.Red else Color.Green,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = leaveMessage,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }

                LaunchedEffect(leaveMessage) {
                    delay(3000)
                    leaveMessage = ""
                }
            }

            if (lliguesCreadesFiltered.isNotEmpty()) {
                Text(
                    text = "Les meves lligues creades:",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                lliguesCreadesFiltered.forEach { lliga ->
                    Card(
                        backgroundColor = Color(0xFF272727),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nom: ${lliga.nomLliga}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Tipus: ${lliga.tipusLliga}", color = Color.LightGray, fontSize = 14.sp)
                            }
                            Button(
                                onClick = { navigateToLligaDetail(lliga) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text("Pilots", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else if (lliguesCreades.isEmpty() && errorCreades.isEmpty()) {
                Text(
                    text = "No tens cap lliga creada",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )
            } else if (errorCreades.isNotEmpty()) {
                Text(
                    text = errorCreades,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else if (lliguesCreades.isNotEmpty() && lliguesCreadesFiltered.isEmpty()) {
                Text(
                    text = "No hi ha lligues ${if (filtreSeleccionat == "PUBLIQUES") "públiques" else "privades"} creades",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (lliguesParticipacioFiltered.isNotEmpty()) {
                Text(
                    text = "Les meves lligues on participo:",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                lliguesParticipacioFiltered.forEach { lliga ->
                    Card(
                        backgroundColor = Color(0xFF272727),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nom: ${lliga.nomLliga}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Tipus: ${lliga.tipusLliga}", color = Color.LightGray, fontSize = 14.sp)
                            }

                            Row {
                                Button(
                                    onClick = { navigateToLligaDetail(lliga) },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCC0000)),
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Text("Pilots", color = Color.White, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                isLeavingLliga = true
                                                httpClient.sortirDeLliga(token, lliga.lligaId)
                                                leaveMessage = "Has sortit de la lliga ${lliga.nomLliga} correctament"

                                                val resposta = httpClient.getLliguesParticipacio(token)
                                                lliguesParticipacio = resposta.lligues
                                            } catch (e: Exception) {
                                                leaveMessage = "Error sortint de la lliga: ${e.message}"
                                            } finally {
                                                isLeavingLliga = false
                                            }
                                        }
                                    },
                                    enabled = !isLeavingLliga,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFF8B0000),
                                        disabledBackgroundColor = Color.Gray
                                    )
                                ) {
                                    if (isLeavingLliga) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text("Sortir", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (lliguesParticipacio.isEmpty() && errorParticipacio.isEmpty()) {
                Text(
                    text = "No participes en cap lliga",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )
            } else if (errorParticipacio.isNotEmpty()) {
                Text(
                    text = errorParticipacio,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else if (lliguesParticipacio.isNotEmpty() && lliguesParticipacioFiltered.isEmpty()) {
                Text(
                    text = "No hi ha lligues ${if (filtreSeleccionat == "PUBLIQUES") "públiques" else "privades"} on participis",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}