package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Jugador(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("posicion")
    val posicion: String?,

    @SerializedName("dorsal")
    val dorsal: Int?,

    @SerializedName("edad")
    val edad: Int?,

    @SerializedName("equipoId")
    val equipoId: Long?
)