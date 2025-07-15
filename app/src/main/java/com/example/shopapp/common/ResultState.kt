package com.example.shopapp.common

sealed class ResultState<out T> {
    data class Success<T>(val data: T): ResultState<T>()
    data class Error(val message: String): ResultState<Nothing>()
    data class Loading(val isLoading: Boolean): ResultState<Nothing>()

}