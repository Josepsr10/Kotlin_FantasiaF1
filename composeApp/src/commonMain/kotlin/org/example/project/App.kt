package org.example.project

import org.example.project.ui.login.LoginPage
import org.example.project.ui.splash.SplashScreen
import androidx.compose.runtime.*
import org.example.project.data.LligaCarpeta.Lliga
import org.example.project.ui.rules.ReglesPage
import org.example.project.ui.HomePage
import org.example.project.ui.PilotsPage
import org.example.project.ui.league.CreateLeaguePage
import org.example.project.ui.league.LeagueOptionsPage
import org.example.project.ui.league.UnirseLligaPage
import org.example.project.ui.perfil.PerfilPage
import org.example.project.ui.register.RegisterPage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var page by remember { mutableStateOf("Splash") }
    var token by remember { mutableStateOf("") }
    var selectedLliga by remember { mutableStateOf<Lliga?>(null) }

    fun updateToken(value: String) {
        token = value
    }

    fun updatePage(value: String) {
        page = value
    }

    fun navigateToLligaDetail(lliga: Lliga) {
        selectedLliga = lliga
        page = "PilotsPage"
    }

    fun navigateToPilots(lliga: Lliga) {
        selectedLliga = lliga
        page = "PilotsPage"
    }

    val splashScreen = SplashScreen(::updatePage)
    val loginPage = LoginPage(::updatePage, ::updateToken)
    val registerPage = RegisterPage(::updatePage)
    val homePage = HomePage(::updatePage, token, ::navigateToLligaDetail, ::navigateToPilots)
    val createLeaguePage = CreateLeaguePage(::updatePage, token)
    val leagueOptionsPage = LeagueOptionsPage(::updatePage, token)
    val reglesPage = ReglesPage(::updatePage)
    val unirseLligaPage = UnirseLligaPage(::updatePage, token)
    val perfilPage = PerfilPage(::updatePage, token)

    // Creamos la instancia de PilotsPage solo si hay una liga seleccionada
    val pilotsPage = selectedLliga?.let {
        PilotsPage(::updatePage, token, it)
    }

    // Selector de pantalla
    when (page) {
        splashScreen.page -> splashScreen.show()
        loginPage.page -> loginPage.show()
        registerPage.page -> registerPage.show()
        homePage.page -> homePage.show()
        createLeaguePage.page -> createLeaguePage.show()
        leagueOptionsPage.page -> leagueOptionsPage.show()
        reglesPage.page -> reglesPage.show()
        unirseLligaPage.page -> unirseLligaPage.show()
        perfilPage.page -> perfilPage.show()
        "PilotsPage" -> {
            pilotsPage?.show() ?: run {
                page = "Home"
            }
        }
    }
}