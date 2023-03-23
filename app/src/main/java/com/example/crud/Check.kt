package com.example.crud

import java.util.*

interface Check {

    fun onSuccess(response: Objects?)

    fun onFailure(response: Objects?)
}