package com.example.uinavegacion.data.network

import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.model.Rival
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // --- GET (Leer) ---
    @GET("api/v1/partidos")
    suspend fun getPartidos(): List<Partido>

    @GET("api/v1/jugadores")
    suspend fun getJugadores(): List<Jugador>

    @GET("api/v1/equipos")
    suspend fun getEquipos(): List<Equipo>

    @GET("api/v1/rivales")
    suspend fun getRivales(): List<Rival>

    // --- POST (Crear) ---
    @POST("api/v1/partidos")
    suspend fun createPartido(@Body partido: Partido): Response<Partido>
}
