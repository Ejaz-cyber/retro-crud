package com.example.crud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.Check2
import com.example.crud.models.StudentModel
import com.example.crud.repository.MainRepository
import com.example.crud.statelistener.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val studentList: MutableLiveData<List<StudentModel>?> = MutableLiveData(emptyList())

    init {
       fetchStudents()
    }

    fun fetchStudents(): Flow<UIState<List<StudentModel>?>> {
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