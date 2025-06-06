package org.example.project.data
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class UsuariInfoResponse(
    val ok: Boolean,
    val message: String,
    val usuari: UsuariInfo
)