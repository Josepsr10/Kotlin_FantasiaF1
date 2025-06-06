package org.example.project.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import f1_fantasia.composeapp.generated.resources.Res
import f1_fantasia.composeapp.generated.resources.logo_fantasia
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource


class SplashScreen(val updatePage: (String) -> Unit) {
    val page = "Splash"

    @Composable
    fun show() {
        LaunchedEffect(Unit) {
            delay(3000)
            updatePage("Login")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1C)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.logo_fantasia),
                contentDescription = "Logo Fantasia",
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
            )
        }
    }
}
