package com.assessment.newspost.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assessment.newspost.model.*
import com.assessment.newspost.network.Network
import com.assessment.newspost.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    private var network: Network = Network()
    private val newsList = MutableLiveData<Resource<List<PostModel>>>()
    private val detailNews = MutableLiveData<Resource<PostModel>>()
    private val userDetail = MutableLiveData<Resource<UserModel>>()
    private val userList = MutableLiveData<Resource<List<UserModel>>>()
    private val commentList = MutableLiveData<Resource<List<CommentModel>>>()
    private val albumList = MutableLiveData<Resource<List<AlbumModel>>>()
    private val photoList = MutableLiveData<Resource<List<PhotoModel>>>()
    private val photDetail = MutableLiveData<Resource<PhotoModel>>()

    fun fetchNews() {
        newsList.postValue(Resource.onLoading(null))
        network.api().showAllPost().enqueue(object : Callback<List<PostModel>>{
            override fun onResponse(
                call: Call<List<PostModel>>,
                response: Response<List<PostModel>>
            ) {
                if (response.isSuccessful) {
                    newsList.postValue(Resource.onSucces(response.body()))
                }
            }

            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                newsList.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getDetailNews(id: Int){
        detailNews.postValue(Resource.onLoading(null))
        network.api().showPost(id).enqueue(object : Callback<PostModel>{
            override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                if (response.isSuccessful) detailNews.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<PostModel>, t: Throwable) {
                detailNews.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun userDetail(id: Int){
        userDetail.postValue(Resource.onLoading(null))
        network.api().detailUser(id).enqueue(object : Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) userDetail.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                userDetail.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getAllUsers(){
        userList.postValue(Resource.onLoading(null))
        network.api().allUser().enqueue(object : Callback<List<UserModel>>{
            override fun onResponse(
                call: Call<List<UserModel>>,
                response: Response<List<UserModel>>
            ) {
                if (response.isSuccessful) userList.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                userList.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getCommentList(id: Int){
        commentList.postValue(Resource.onLoading(null))
        network.api().showAllComments(id).enqueue(object : Callback<List<CommentModel>>{
            override fun onResponse(
                call: Call<List<CommentModel>>,
                response: Response<List<CommentModel>>
            ) {
                if (response.isSuccessful) commentList.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<List<CommentModel>>, t: Throwable) {
                commentList.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getAlbumList(id: Int){
        albumList.postValue(Resource.onLoading(null))
        network.api().showAlbumList(id).enqueue(object : Callback<List<AlbumModel>>{
            override fun onResponse(
                call: Call<List<AlbumModel>>,
                response: Response<List<AlbumModel>>
            ) {
                if (response.isSuccessful) albumList.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<List<AlbumModel>>, t: Throwable) {
                albumList.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getPhotoList(id: Int){
        photoList.postValue(Resource.onLoading(null))
        network.api().showPhotoList(id).enqueue(object : Callback<List<PhotoModel>>{
            override fun onResponse(
                call: Call<List<PhotoModel>>,
                response: Response<List<PhotoModel>>
            ) {
                if (response.isSuccessful) photoList.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<List<PhotoModel>>, t: Throwable) {
                photoList.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getPhotoDetail(id: Int){
        photDetail.postValue(Resource.onLoading(null))
        network.api().detailPhoto(id).enqueue(object : Callback<PhotoModel>{
            override fun onResponse(call: Call<PhotoModel>, response: Response<PhotoModel>) {
                if (response.isSuccessful) photDetail.postValue(Resource.onSucces(response.body()))
            }

            override fun onFailure(call: Call<PhotoModel>, t: Throwable) {
                photDetail.postValue(Resource.onError(t.message, null))
            }

        })
    }

    fun getNews() : LiveData<Resource<List<PostModel>>> = newsList

    fun getNewsData() : LiveData<Resource<PostModel>> = detailNews

    fun getUser() : LiveData<Resource<UserModel>> = userDetail

    fun getUsersList() : LiveData<Resource<List<UserModel>>> = userList

    fun getCommentData() : LiveData<Resource<List<CommentModel>>> = commentList

    fun getAlbumData() : LiveData<Resource<List<AlbumModel>>> = albumList

    fun getPhotoData() : LiveData<Resource<List<PhotoModel>>> = photoList

    fun getPhoto() : LiveData<Resource<PhotoModel>> = photDetail
}