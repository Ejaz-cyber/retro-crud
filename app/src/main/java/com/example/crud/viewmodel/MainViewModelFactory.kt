package com.example.crud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crud.repository.MainRepository

/**
 * No longer needed here coz this is implemented using HILT in ApplicationModule
 */
class MainViewModelFactory (private val repo: MainRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}