package com.assessment.newspost.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.newspost.adapter.NewsAdapter
import com.assessment.newspost.R
import com.assessment.newspost.databinding.ActivityMainBinding
import com.assessment.newspost.model.PostModel
import com.assessment.newspost.model.UserModel
import com.assessment.newspost.utils.Status
import com.assessment.newspost.utils.setVisible
import com.assessment.newspost.utils.toasShort
import com.assessment.newspost.viewmodel.NewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewsList : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsadapter: NewsAdapter
    private lateinit var dialogBinding: BottomSheetDialog
    private var listNews : ArrayList<PostModel> = ArrayList()
    private var listUser : ArrayList<UserModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupViewModel()
    }

    private fun initView(){
        dialogBinding = BottomSheetDialog(this, R.style.DialogSave)
        newsViewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        binding.swrList.setOnRefreshListener {
            newsViewModel.fetchNews()
            newsViewModel.getAllUsers()
        }
    }

    private fun setupViewModel(){
        newsViewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        getAllNews()
    }

    private fun getAllNews(){
        newsViewModel.fetchNews()
        newsViewModel.getAllUsers()
        newsViewModel.getNews().observe(this, Observer {
            when(it.status){
                Status.SUCCES -> {
                    toasShort("Sukses")
                    listNews.clear()
                    listNews.addAll(it.data?.toCollection(ArrayList()) ?: emptyList())
                    Log.d("news list", listNews.toString())
                    setListNews(listNews)
                    showLoading(false)
                    //binding.rvNews.setVisible(true)
                }
                Status.LOADING -> {
                    if (listNews.isNotEmpty()) listNews.clear()
                    //binding.rvNews.setVisible(false)
                    showLoading(true)
                }
                Status.ERROR -> {
                    toasShort(it.message.toString())
                    showLoading(false)
                }
            }
        })

        newsViewModel.getUsersList().observe(this, Observer {
            when(it.status){
                Status.SUCCES -> {
                    toasShort("Sukses")
                    listUser.clear()
                    listUser.addAll(it.data?.toCollection(ArrayList()) ?: emptyList())
                    showLoading(false)
                }
                Status.LOADING -> {
                    if (listNews.isNotEmpty()) listNews.clear()
                    showLoading(true)
                }
                Status.ERROR -> {
                    toasShort(it.message.toString())
                    showLoading(false)
                }
            }
        })

    }

    private val listener = object : NewsAdapter.NewsListener {
        override fun onClick(news: PostModel, user: UserModel) {
            startActivity(DetailActivity.newIntent(this@NewsList, news.id ?: 0, user.name.toString()))
        }



    }
    private fun setListNews(data: List<PostModel>){
        val newUser = ArrayList<UserModel>()
        if (listUser.size > 0){
            data.forEach{ post ->
                listUser.forEach { user ->
                    if (post.userId == user.id){
                        newUser.add(user)
                    }
                }
            }
            newsadapter = NewsAdapter(data.toCollection(ArrayList()), newUser, listener)
            binding.rvNews.adapter = newsadapter
        }

        binding.rvNews.layoutManager = LinearLayoutManager(this)

    }

    private fun showLoading(show: Boolean){
        binding.swrList.isRefreshing = show
    }
}