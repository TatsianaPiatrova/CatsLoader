package com.example.catsloader.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catsloader.Repository
import com.example.catsloader.model.Cat
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    
    val myResponse: MutableLiveData<List<Cat>> = MutableLiveData()
    
    fun getCat(){
        viewModelScope.launch{
            val response : List<Cat> = repository.getCat()
            myResponse.value = response
        }
    }
}