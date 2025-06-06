package org.example.project.ui.rules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.rememberCoroutineScope

class ReglesPage(val updatePage: (String) -> Unit) {
    val page = "Regles"

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
                        title = { Text("Regles", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
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
                    ReglesContent(Modifier.padding(paddingValues))
                },
                backgroundColor = Color(0xFF1C1C1C)
            )
        }
    }
    @Composable
    fun ReglesContent(modifier: Modifier = Modifier) {
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1C))
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                "Pèrdua de punts:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                """
                Límits del circuit:
                Si un pilot supera els límits de pista, serà penalitzat amb -3 punts.

                Parada a boxes:
                Si un pilot té una sanció de 5 segons per qualsevol motiu i, en aturar-se a boxes, es toca el cotxe abans de complir els 5 segons, serà penalitzat amb -2 punts.

                Velocitat a boxes:
                Si un pilot supera el límit de velocitat a la parada de boxes, serà penalitzat amb -5 punts.

                Línia de boxes:
                Si un pilot, en entrar a boxes, trepitja la línia d’entrada, serà sancionat amb -1 punt.

                5 segons:
                Si un pilot rep una penalització de 5 segons, serà penalitzat amb -2 punts. Si rep 10 segons, serien -4 punts. Cada 5 segons equivalen a -2 punts.

                Bandera negra:
                Si a un pilot li treuen la bandera negra, serà penalitzat amb -20 punts.

                DNF:
                Si el pilot abandona la carrera, no se li comptarà cap punt, encara que hagi puntuat abans.

                Drive-through:
                Si un pilot rep una sanció de drive-through, serà penalitzat amb -5 punts.
                """.trimIndent(),
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                "Punts:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                """
                Resultat de qualificació:
                1º     10 punts
                2º      9 punts
                3º      8 punts
                4º      7 punts
                5º      6 punts
                6º      5 punts
                7º      4 punts
                8º      3 punts
                9º      2 punts
                10º     1 punt

                Volta ràpida de la carrera: +3 punts
                Pilot del dia: +1 punt
                Parada més ràpida de boxes: +1 punt
                Grand Chelem: +7 punts
                - Volta ràpida
                - Liderar totes les voltes primer.
                - 1r posició

                *No es comptarà el sistema de puntuació de la Clasificació, Sprint o tot el que no sigui carrera.*
                """.trimIndent(),
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
