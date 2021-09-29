package com.example.catsloader

import com.example.catsloader.model.Cat

class Repository {

    suspend fun getCat() : List<Cat> {
        return RetrofitInstance.api.getListOfCats()
    }
}