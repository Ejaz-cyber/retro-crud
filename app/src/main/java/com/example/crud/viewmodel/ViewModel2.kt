package com.example.crud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.Check
import com.example.crud.Check2
import com.example.crud.models.StudentModelPost
import com.example.crud.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel2 @Inject constructor(private val repository: MainRepository) : ViewModel() {
    fun postStudent(studentModelPost: StudentModelPost, check: Check) {
        viewModelScope.launch {
            repository.postStudent(check, studentModelPost)

        }
    }

    fun updateStudent(id: String, studentModelPost: StudentModelPost,check: Check2) {
        viewModelScope.launch {
            repository.updateStudent(id, studentModelPost, check)
        }
    }

}