package com.example.uinavegacion.data.model

import com.google.gson.annotations.SerializedName

data class Rival(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val nombre: String
)
