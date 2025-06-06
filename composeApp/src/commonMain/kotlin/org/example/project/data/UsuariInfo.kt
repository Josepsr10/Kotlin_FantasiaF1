package org.example.project.data
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class UsuariInfo(
    val usuariId: String,
    val nomUsuari: String,
    val correuElectronic: String,
    val rol: String,
    val imatgePerfil: Boolean? = null
)