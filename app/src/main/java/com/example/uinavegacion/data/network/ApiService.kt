package com.example.uinavegacion.data.network

import com.example.uinavegacion.data.model.Equipo
import com.example.uinavegacion.data.model.Jugador
import com.example.uinavegacion.data.model.Partido
import com.example.uinavegacion.data.model.Rival
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // --- GET (Leer) ---
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

    @POST("jugadores")
    suspend fun createJugador(@Body jugador: Jugador): Response<Jugador>

    @POST("equipos")
    suspend fun createEquipo(@Body equipo: Equipo): Response<Equipo>

    @POST("rivales")
    suspend fun createRival(@Body rival: Rival): Response<Rival>

    // --- POST (Actualizar) ---
    @POST("partidos/actualizar")
    suspend fun updatePartido(@Body partido: Partido): Response<Partido>

    @POST("jugadores/actualizar")
    suspend fun updateJugador(@Body jugador: Jugador): Response<Jugador>

    @POST("equipos/actualizar")
    suspend fun updateEquipo(@Body equipo: Equipo): Response<Equipo>

    @POST("rivales/actualizar")
    suspend fun updateRival(@Body rival: Rival): Response<Rival>

    // --- DELETE (Eliminar) ---
    @DELETE("partidos/eliminar/{id}")
    suspend fun deletePartido(@Path("id") id: Long): Response<Void>

    @DELETE("jugadores/eliminar/{id}")
    suspend fun deleteJugador(@Path("id") id: Long): Response<Void>

    @DELETE("equipos/eliminar/{id}")
    suspend fun deleteEquipo(@Path("id") id: Long): Response<Void>

    @DELETE("rivales/eliminar/{id}")
    suspend fun deleteRival(@Path("id") id: Long): Response<Void>
}
