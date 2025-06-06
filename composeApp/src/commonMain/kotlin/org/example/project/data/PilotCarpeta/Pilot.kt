package org.example.project.data.PilotCarpeta

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Pilot(
    val id: String,
    val nom: String,
    val edat: Int,
    val nacionalitat: String,
    val punts: Int = 0,
    val equip: String,
    val imgUrl: String = ""
)