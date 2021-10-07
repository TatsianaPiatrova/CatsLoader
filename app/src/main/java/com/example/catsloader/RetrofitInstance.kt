package com.example.catsloader

import com.example.catsloader.util.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}