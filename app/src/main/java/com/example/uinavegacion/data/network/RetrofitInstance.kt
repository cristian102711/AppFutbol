package com.example.uinavegacion.data.network

import com.example.uinavegacion.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Ya no usamos una constante. La URL se toma desde el BuildConfig generado por Gradle.
    private val BASE_URL = BuildConfig.API_BASE_URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
