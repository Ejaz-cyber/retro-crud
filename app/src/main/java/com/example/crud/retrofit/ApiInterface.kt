package com.example.crud.retrofit

import com.example.crud.models.StudentModel
import com.example.crud.models.StudentModelPost
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.Objects

interface ApiInterface {
    @GET("students/")
    suspend fun getStudents() : Response<List<StudentModel>>

    @POST("students/")
    suspend fun postStudent(@Body studentModel: StudentModelPost) : Response<Objects>


    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id : String, @Body studentModel: StudentModelPost) : Response<Unit>


    @DELETE("students/{id}")
    suspend fun deleteUser(@Path("id") id : String) : Response<Unit>

}