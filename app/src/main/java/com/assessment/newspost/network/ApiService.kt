package com.assessment.newspost.network

import com.assessment.newspost.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("posts")
    fun showAllPost() : Call<List<PostModel>>

    @GET("posts/{id}")
    fun showPost(@Path("id") id: Int) : Call<PostModel>

    @GET("users")
    fun allUser(): Call<List<UserModel>>

    @GET("users/{id}")
    fun detailUser(@Path("id") id: Int) : Call<UserModel>

    @GET("posts/{id}/comments")
    fun showAllComments(@Path("id") id : Int) : Call<List<CommentModel>>

    @GET("users/{id}/albums")
    fun showAlbumList(@Path("id") id: Int) : Call<List<AlbumModel>>

    @GET("albums/{id}/photos")
    fun showPhotoList(@Path("id") id: Int) : Call<List<PhotoModel>>

    @GET("photos/{id}")
    fun detailPhoto(@Path("id") id: Int) : Call<PhotoModel>
}
