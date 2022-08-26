package com.veit.app.weatherino.utils

sealed class Resource<T>(
    val status: Status,
    val data: T? = null
) {
    class Success<T>(data: T): Resource<T>(Status.SUCCESS, data)
    class Error<T>: Resource<T>(Status.ERROR, null)
    class Loading<T>: Resource<T>(Status.LOADING, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (status != other.status) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }


}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}