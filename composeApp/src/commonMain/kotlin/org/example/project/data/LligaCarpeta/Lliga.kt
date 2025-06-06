package org.example.project.data.LligaCarpeta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys

data class Lliga(
    val lligaId: String,
    val nomLliga: String,
    val tipusLliga: TipusLliga,
    val creadorId: String? = null, //opcional pero obligatorio en API
    val contrasenya: String? = null // si es opcional
)
