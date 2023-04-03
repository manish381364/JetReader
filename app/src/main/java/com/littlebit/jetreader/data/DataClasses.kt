package com.littlebit.jetreader.data

sealed class Resource<T>(val data: T? = null, val message: String? =null){
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String?, data: T? = null): Resource<T>(data, message )
    class Loading<T>(data: T? = null): Resource<T>(data)
}

data class DataOrException<T, Boolean, E: java.lang.Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: E? = null,
)