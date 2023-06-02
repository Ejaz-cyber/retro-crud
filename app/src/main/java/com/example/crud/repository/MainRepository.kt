package com.example.crud.repository

import android.util.Log
import com.example.crud.Check
import com.example.crud.Check2
import com.example.crud.models.StudentModelPost
import com.example.crud.retrofit.ApiInterface
import com.example.crud.statelistener.UIState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository(private val apiInterface: ApiInterface) {
    fun getStudents() = flow {
        Log.e("Repository", "api called - GET")

        emit(UIState.Loading)
        val data = apiInterface.getStudents()
        if (data.isSuccessful) {
            emit(UIState.Success(data.body()))
            return@flow
        }
    }.catch { emit(UIState.Error(it.message.toString())) }

    suspend fun postStudent(check: Check, studentModelPost: StudentModelPost) {
        Log.e("Repository", "api called - POST")

        val response = apiInterface.postStudent(studentModelPost)
        if (response.isSuccessful) {
            Log.i("MainRepo", "postStudent: student added")
            check.onSuccess(response.body())
        } else {
            Log.e("MainRepo", "postStudent: Error - ${response.message()}")
            check.onFailure(response.body())
        }
    }

    suspend fun updateStudent(id: String, studentModelPost: StudentModelPost, check: Check2) {
        Log.e("Repository", "api called - PUT")

        val response = apiInterface.updateStudent(id, studentModelPost)
        if (response.isSuccessful) {
            Log.i("MainRepo", "postStudent: student edited")
            check.onSuccess(response.body())
        } else {
            Log.e("MainRepo", "updateStudent: Error - ${response.message()}")
            check.onFailure(response.body())
        }
    }

    suspend fun deleteStudent(id: String, check: Check2) {
        Log.e("Repository", "api called - DELETE")

        val response = apiInterface.deleteUser(id)
        if (response.isSuccessful) {
            Log.i("MainRepo", "deleteStudent: Success")
            check.onSuccess(response.body())
        } else {
            Log.i("MainRepo", "deleteStudent: Error - ${response.message()}")
            check.onFailure(response.body())
        }
    }

}