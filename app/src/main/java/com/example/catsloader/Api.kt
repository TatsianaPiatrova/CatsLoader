package com.example.catsloader

import com.example.catsloader.model.Cat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/v1/images/search")
    fun getListOfCats(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Call<ArrayList<Cat>>
}