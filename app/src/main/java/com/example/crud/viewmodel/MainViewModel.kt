package com.example.crud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.Check
import com.example.crud.Check2
import com.example.crud.models.StudentModel
import com.example.crud.models.StudentModelPost
import com.example.crud.repository.MainRepo

import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepo) : ViewModel() {

    init {
        fetchStudents()
    }

    fun fetchStudents() {
        viewModelScope.launch {
            repository.getStudents()
        }
    }

    val studentList: LiveData<List<StudentModel>>
        get() = repository.students


    suspend fun deleteStudent(id: String, check: Check2) {
        viewModelScope.launch {
            val job = viewModelScope.launch {
                repository.deleteStudent(id, check)
            }
            job.join()
            repository.getStudents()
        }


    }

}