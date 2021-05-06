package com.example.aymericjulien.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Api {
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1MjYsImV4cCI6MTY1MTA0MTM4MH0.uCZTYBE6mhvnhuiqfk_o1qwKMmwx5RTylpAmpghqR0Y"

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }

}