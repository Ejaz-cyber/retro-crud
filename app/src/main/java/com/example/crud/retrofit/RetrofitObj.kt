package com.example.crud.retrofit

import com.example.crud.utils.BASEURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * No longer needed here coz this is implemented using HILT in ApplicationModule
 */
object RetrofitObj {
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}