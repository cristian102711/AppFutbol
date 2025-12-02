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
    // Usamos 'suspend' y devolvemos 'Response' para manejar isSuccessful en el repositorio

    @GET("partidos")
    suspend fun getPartidos(): Response<List<Partido>>

    @GET("jugadores")
    suspend fun getJugadores(): Response<List<Jugador>>

    @GET("equipos")
    suspend fun getEquipos(): Response<List<Equipo>>

    @GET("rivales")
    suspend fun getRivales(): Response<List<Rival>>

    // --- POST (Crear) ---
    @POST("partidos")
    suspend fun createPartido(@Body partido: Partido): Response<Partido>
}