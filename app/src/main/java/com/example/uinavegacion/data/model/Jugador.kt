package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Jugador(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val nombre: String,

    @SerializedName("position")
    val posicion: String,

    @SerializedName("skill_level")
    val nivel: Int
)
