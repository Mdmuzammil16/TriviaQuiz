package com.staffofyuser.staffofyuser.Api


sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, errorData: T? = null) : NetworkResult<T>(errorData, message)
    class Loading<T> : NetworkResult<T>()

}