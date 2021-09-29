package com.example.catsloader

import com.example.catsloader.model.Cat
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/v1/images/search")
    suspend fun getListOfCats(): List<Cat>
}