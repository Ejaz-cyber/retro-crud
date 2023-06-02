package com.example.crud.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.Check2
import com.example.crud.models.StudentModel
import com.example.crud.repository.MainRepository
import com.example.crud.statelistener.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val studentList: MutableLiveData<List<StudentModel>> = MutableLiveData(emptyList())
//        get() = repository.students.value


    init {
//        studentList.postValue(fetchStudents())
        fetchStudents()
    }

    fun fetchStudents() : Flow<UIState<List<StudentModel>?>> {
        return repository.getStudents().flowOn(Dispatchers.IO)
    }

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