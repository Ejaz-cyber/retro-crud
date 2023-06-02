package com.example.crud.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    // change this api with new crudcrud.com's api for another 24hr use
    private val BASEURL = "https://crudcrud.com/api/576664abb6bc49899573044113a53e51/"

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}