package com.example.crud.models

import com.google.gson.annotations.SerializedName

data class StudentModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val age: Int,
    val collegeName: String,
    val batch: String,
    val phone: String
) {
}