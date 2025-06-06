package org.example.project.data.RegisterCarpeta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Register (
    val usuariId: String,
    val nomUsuari: String,
    val correuElectronic: String,
    val contrasenyaUsuari: String? = null,
    val imatgePerfil: Boolean,
    val rol: String
)