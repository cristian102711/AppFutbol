package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Partido(
    @SerializedName("id")
    val id: Long,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("rival")
    val nombreRival: String,

    @SerializedName("resultado")
    val resultado: String
)
