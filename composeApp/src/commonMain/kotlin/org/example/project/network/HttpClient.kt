package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import org.example.project.data.*
import org.example.project.data.LligaCarpeta.LligaUsuari
import org.example.project.data.PilotCarpeta.PilotsInfoResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HttpClient {
    private val client = HttpClient()
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    val ip = "http://192.168.50.248:5005"

    @OptIn(ExperimentalUuidApi::class)
    suspend fun postRegister(nom: String, email: String, password: String): Boolean {
        try {
            val id = Uuid.random().toString()
            val body = buildJsonObject {
                put("nom", nom)
                put("email", email)
                put("password", password)
            }

            val response = client.post("$ip/api/register/") {
                contentType(ContentType.Application.Json)
                setBody(body.toString())
            }

            return response.status.value == 200
        } catch (e: Exception) {
            throw Exception("Error de registro: ${e.message}")
        }
    }

    suspend fun postLogin(email: String, password: String): String {
        try {
            val body = buildJsonObject {
                put("email", email)
                put("password", password)
            }

            val response = client.post("$ip/api/login") {
                contentType(ContentType.Application.Json)
                setBody(body.toString())
            }

            if (response.status.value != 200) {
                throw Exception("Credenciales incorrectas")
            }

            val token = Json.decodeFromString<Token>(response.bodyAsText())
            return token.token
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }
    @OptIn(ExperimentalUuidApi::class)
    suspend fun postCreateLliga(token: String, nomLliga: String, tipusLliga: String, contrasenya: String?): Boolean {
        try {
            val lligaId = Uuid.random().toString()
            val body = buildJsonObject {
                put("nomLliga", nomLliga)
                put("tipusLliga", tipusLliga)
                put("contrasenya", contrasenya)
            }

            val response = client.post("$ip/api/lliga") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(body.toString())
            }

            return response.status.value == 200 || response.status.value == 201
        } catch (e: Exception) {
            throw Exception("Error al crear la lliga: ${e.message}")
        }
    }
    suspend fun getLligues(token: String): LligaUsuari {
        val response = client.get("$ip/api/lligues") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        if (response.status.value != 200) {
            throw Exception("No s'han pogut carregar les lligues")
        }
        return Json.decodeFromString<LligaUsuari>(response.bodyAsText())
    }
    suspend fun unirALliga(token: String, lligaId: String, contrasenya: String?): Boolean {
        try {
            val body = buildJsonObject {
                put("contrasenya", contrasenya)
            }

            val response = client.post("$ip/api/lliga/$lligaId/unirse") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(body.toString())
            }

            return response.status.value == 200 || response.status.value == 201
        } catch (e: Exception) {
            throw Exception("Error al unir-se a la lliga: ${e.message}")
        }
    }
    suspend fun patchUpdateNom(token: String, nouNom: String): String {
        try {
            val body = buildJsonObject {
                put("nouNom", nouNom)
            }

            val response = client.patch("$ip/api/usuari/update-nom") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(body.toString())
            }

            if (response.status.value == 200) {
                return "Nom actualitzat correctament."
            }

            throw Exception(Json.parseToJsonElement(response.bodyAsText())
                .jsonObject["message"]?.jsonPrimitive?.content ?: "Error inesperat")
        } catch (e: Exception) {
            throw Exception("Error actualitzant nom: ${e.message}")
        }
    }

    suspend fun patchUpdatePassword(token: String, password: String, nouPassword: String): String {
        try {
            val body = buildJsonObject {
                put("password", password)
                put("nouPassword", nouPassword)
            }

            val response = client.patch("$ip/api/usuari/update-password") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(body.toString())
            }

            if (response.status.value == 200) {
                return "Contrasenya actualitzada correctament."
            }

            throw Exception(Json.parseToJsonElement(response.bodyAsText())
                .jsonObject["message"]?.jsonPrimitive?.content ?: "Error inesperat")
        } catch (e: Exception) {
            throw Exception("Error actualitzant contrasenya: ${e.message}")
        }
    }
    suspend fun sortirDeLliga(token: String, lligaId: String): Boolean {
        val response = client.delete("$ip/api/lliga/$lligaId/deixar-lliga") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status.value != 200) {
            throw Exception("Error sortint de la lliga.")
        }

        return true
    }
    suspend fun getLliguesUsuari(token: String): LligaUsuari {
        val response = client.get("$ip/api/usuari/lligues") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }


        if (response.status.value != 200) {
            throw Exception("Error carregant lligues de l'usuari")
        }

        println(response.bodyAsText())

        return Json.decodeFromString<LligaUsuari>(response.bodyAsText())
    }
    suspend fun getLliguesParticipacio(token: String): LligaUsuari {
        val response = client.get("$ip/api/lligues/participacio") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        if (response.status.value != 200) {
            throw Exception("No s'han pogut carregar les lligues on participo")
        }
        return Json.decodeFromString<LligaUsuari>(response.bodyAsText())
    }
    suspend fun getUsuariInfo(token: String): UsuariInfoResponse {
        val response = client.get("$ip/api/usuari-info") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        if (response.status.value != 200) {
            throw Exception("No s'ha pogut carregar la informació de l'usuari")
        }
        return Json.decodeFromString<UsuariInfoResponse>(response.bodyAsText())
    }
    suspend fun getPilotsInfo(token: String, lligaId: String): PilotsInfoResponse {
        return try {
            val response = client.get("$ip/api/pilots-info/$lligaId") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            response.body()
        } catch (e: Exception) {
            println("Error en getPilotsInfo: ${e.message}")
            throw e
        }
    }


}