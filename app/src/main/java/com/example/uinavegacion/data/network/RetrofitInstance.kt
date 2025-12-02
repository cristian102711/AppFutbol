package com.example.uinavegacion.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {


    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    private val retrofitEquipos by lazy {
        Retrofit.Builder()
            .baseUrl("https://ms-equipos.onrender.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiEquipos: ApiService by lazy {
        retrofitEquipos.create(ApiService::class.java)
    }


    private val retrofitPartidos by lazy {
        Retrofit.Builder()
            .baseUrl("https://ms-partidos.onrender.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiPartidos: ApiService by lazy {
        retrofitPartidos.create(ApiService::class.java)
    }


    private val retrofitJugadores by lazy {
        Retrofit.Builder()
            .baseUrl("https://ms-jugadores.onrender.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiJugadores: ApiService by lazy {
        retrofitJugadores.create(ApiService::class.java)
    }


    private val retrofitRivales by lazy {
        Retrofit.Builder()
            .baseUrl("https://ms-rivales.onrender.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiRivales: ApiService by lazy {
        retrofitRivales.create(ApiService::class.java)
    }
}