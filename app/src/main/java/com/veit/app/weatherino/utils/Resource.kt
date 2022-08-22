package com.veit.app.weatherino.utils

sealed class Resource<T>(
    val status: Status,
    val data: T? = null
) {
    class Success<T>(data: T): Resource<T>(Status.SUCCESS, data)
    class Error<T>: Resource<T>(Status.ERROR, null)
    class Loading<T>: Resource<T>(Status.LOADING, null)
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}