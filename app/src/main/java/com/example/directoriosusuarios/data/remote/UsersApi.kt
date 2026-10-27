package com.example.directoriosusuarios.data.remote

import com.example.directoriosusuarios.data.model.Post
import com.example.directoriosusuarios.data.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("users") suspend fun getUsers(): Response<List<User>>
    @GET("posts") suspend fun getPosts(@Query("userId") userId: Int): Response<List<Post>>
}

object ApiClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()
    val service: UsersApi = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(UsersApi::class.java)
}
