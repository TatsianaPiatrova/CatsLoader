package com.example.catsloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.catsloader.databinding.MainActivityBinding
import com.example.catsloader.databinding.MainActivityBinding.inflate
import com.example.catsloader.ui.main.MainViewModel
import com.example.catsloader.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.button.setOnClickListener{
            viewModel.getCat()
            viewModel.myResponse.observe(this, Observer { response ->
                binding.textView.text = response[0].id
                binding.textView2.text = response[0].url
            })
        }
    }
}