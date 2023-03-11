package retrofit

import models.StudentModel
import models.StudentModelPost
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    https://crudcrud.com/api/ba8d109ddfaa4c3487eab6948561de3a/users
    @GET("users")
    fun getAllStudents() : Call<ArrayList<StudentModel>>


    @POST("users/")
    fun postStudent(@Body studentModel: StudentModelPost) : Call<StudentModelPost>


//    @PUT("users/{id}")
//    fun updateStudent(@Path("id") id : String, @Body studentModel: StudentModelPost) : Call<StudentModelPost>


    @PUT("users/{id}")
    fun updateStudent(@Path("id") id : String, @Body studentModel: StudentModelPost) : Call<Void>


    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id : String) : Call<Void>

}