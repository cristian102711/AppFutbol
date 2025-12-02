package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Partido(
    @SerializedName("id")
    val id: Long,

    @SerializedName("fecha")
    val fecha: String?,

    // El JSON trae "rivalId", NO el nombre.
    @SerializedName("rivalId")
    val rivalId: Long?,

    @SerializedName("resultado")
    val resultado: String?,

    @SerializedName("golesFavor")
    val golesFavor: Int?,

    @SerializedName("golesContra")
    val golesContra: Int?
) {
    // Propiedad auxiliar para que HomeScreen no se rompa mientras tanto
    val nombreRivalMostrar: String
        get() = "Rival ID: ${rivalId ?: "?"}"
}