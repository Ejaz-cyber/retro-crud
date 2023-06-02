package com.example.crud.statelistener

sealed interface UIState<out T> {

    data class Success<T>(val data: T) : UIState<T>

    data class Error(val message: String) : UIState<Nothing>

    object Loading : UIState<Nothing>
}