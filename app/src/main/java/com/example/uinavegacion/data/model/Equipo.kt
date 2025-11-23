package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Equipo(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val nombre: String,

    @SerializedName("captain_id")
    val capitanId: Long
)
