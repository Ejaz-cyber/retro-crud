package com.example.crud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crud.repository.MainRepo

class ViewModel2Factory (private val repo: MainRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModel2(repo) as T
    }
}