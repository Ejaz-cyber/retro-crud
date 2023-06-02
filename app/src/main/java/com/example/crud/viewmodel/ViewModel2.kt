package com.example.crud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.Check
import com.example.crud.Check2
import com.example.crud.models.StudentModelPost
import com.example.crud.repository.MainRepository
import kotlinx.coroutines.launch

class ViewModel2 (private val repo: MainRepository) : ViewModel() {
    fun postStudent(studentModelPost: StudentModelPost, check: Check) {
        viewModelScope.launch {
            repo.postStudent(check, studentModelPost)

        }
    }

    fun updateStudent(id: String, studentModelPost: StudentModelPost,check: Check2) {
        viewModelScope.launch {
            repo.updateStudent(id, studentModelPost, check)
        }
    }

}