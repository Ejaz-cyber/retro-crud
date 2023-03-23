package com.example.crud.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crud.Check
import com.example.crud.Check2
import com.example.crud.models.StudentModel
import com.example.crud.models.StudentModelPost
import com.example.crud.retrofit.ApiInterface

class MainRepo(private val apiInterface: ApiInterface) {

    private val studentsLiveData = MutableLiveData<List<StudentModel>>()

    val students: LiveData<List<StudentModel>>
        get() = studentsLiveData

    suspend fun getStudents() {
        val response = apiInterface.getStudents()
        if(response.isSuccessful) {
            studentsLiveData.value = response.body()
            Log.i("MainRepo", "getStudents: student fetched")
        }else{
            Log.i("MainRepo", "getStudents: Error - ${response.message()}")
        }
    }

    suspend fun postStudent(check: Check, studentModelPost: StudentModelPost) {
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
        val response = apiInterface.updateStudent(id, studentModelPost)
        if (response.isSuccessful) {
            Log.i("MainRepo", "postStudent: student edited")
            check.onSuccess(response.body())
        } else {
            Log.e("MainRepo", "updateStudent: Error - ${response.message()}")
            check.onFailure(response.body())
        }


        /*
        val call : Call<Void> = apiInterface.updateStudent(id, studentModelPost)
        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Log.e("mainrepo", "onResponse: updated" )            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.e("mainrepo", "onResponse: ${t.localizedMessage}" )
            }
        })
        */

    }

    suspend fun deleteStudent(id: String, check: Check2) {
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