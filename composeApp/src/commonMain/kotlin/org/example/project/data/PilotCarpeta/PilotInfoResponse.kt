package org.example.project.data.PilotCarpeta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PilotsInfoResponse(
    val ok: Boolean,
    val message: String,
    val data: PilotsData
)