package com.assessment.newspost.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?){
    companion object {
        fun <T> onSucces(data: T?) : Resource<T>{
            return Resource(Status.SUCCES, data, null)
        }

        fun <T> onError(msg: String?, data: T?): Resource<T>{
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> onLoading(data: T?) : Resource<T>{
            return Resource(Status.LOADING, data, null)
        }
    }
}
