package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Equipo(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre") // ¡Antes decía "name"!
    val nombre: String?,

    @SerializedName("entrenador")
    val entrenador: String?,

    @SerializedName("escudoUrl")
    val escudoUrl: String?
)