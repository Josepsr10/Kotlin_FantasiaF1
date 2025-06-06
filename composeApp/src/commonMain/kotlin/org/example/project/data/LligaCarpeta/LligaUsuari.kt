package org.example.project.data.LligaCarpeta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys

data class LligaUsuari(
    val ok: Boolean,
    val lligues: List<Lliga>
)