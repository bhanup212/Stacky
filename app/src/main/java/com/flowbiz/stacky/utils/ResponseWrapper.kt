package com.flowbiz.stacky.utils

sealed class ResponseWrapper<T> {
    class Success<T>(val data: T) : ResponseWrapper<T>()
    class Error<T>(val errorCode: Int, val message: String) : ResponseWrapper<T>()
}