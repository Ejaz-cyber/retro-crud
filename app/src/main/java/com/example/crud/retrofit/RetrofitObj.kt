package com.example.crud.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    // change this api with new crudcrud.com's api for another 24hr use
    private val BASEURL = "https://crudcrud.com/api/84f58fe5d25949f3bbb1bba238413d9e/"

    fun getRetrofitInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}