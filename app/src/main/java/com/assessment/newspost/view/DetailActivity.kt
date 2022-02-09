package com.assessment.newspost.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.newspost.R
import com.assessment.newspost.adapter.CommentAdapter
import com.assessment.newspost.databinding.ActivityDetailBinding
import com.assessment.newspost.model.CommentModel
import com.assessment.newspost.model.PostModel
import com.assessment.newspost.model.UserModel
import com.assessment.newspost.utils.Status
import com.assessment.newspost.utils.setVisible
import com.assessment.newspost.utils.toasShort
import com.assessment.newspost.viewmodel.NewsViewModel
import org.jsoup.Jsoup

class DetailActivity : AppCompatActivity() {

    companion object{
        private const val DETAIL_KEY = "detail"
        private const val USER_KEY  = "USER"
        fun newIntent(context: Context, id: Int, userName: String): Intent{
            val i = Intent(context, DetailActivity::class.java)
            i.putExtra(DETAIL_KEY, id)
            i.putExtra(USER_KEY, userName)
            return i
        }
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var newsViewModel: NewsViewModel
    private var idNews: Int = 0
    private var idUser: Int = 0
    private lateinit var userName: String
    private var listComment: ArrayList<CommentModel> = ArrayList()
    private lateinit var commentAdapter: CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        idNews = intent.getIntExtra(DETAIL_KEY, 0)
        userName = intent.getStringExtra(USER_KEY).toString()
        commentAdapter = CommentAdapter()
        setContentView(binding.root)
        setupViewModel()
    }

    private fun setupViewModel(){
        newsViewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        newsViewModel.getDetailNews(idNews)
        newsViewModel.getCommentList(idNews)
        getCommentList { list ->
            listComment.clear()
            listComment.addAll(list)
            showListComment(listComment)
        }

        getNewsDetail {
            showDetail(it)
            idUser = it.userId ?: 0
        }

        binding.swrDetail.setOnRefreshListener {
            newsViewModel.getDetailNews(idNews)
            newsViewModel.getCommentList(idNews)
        }

        binding.tvUser.setOnClickListener {
            startActivity(UserActivity.newIntent(this, idUser))
        }

        binding.commentTitle.setOnClickListener {
            when(binding.rvComment.isVisible){
                true -> {
                    binding.rvComment.setVisible(false)
                    binding.commentTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_arrow_down), null)
                }
                false -> {
                    binding.rvComment.setVisible(true)
                    binding.commentTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_arrow_up), null)
                }
            }
        }
    }

    fun getNewsDetail(callbacks: (PostModel) -> Unit){
        newsViewModel.getNewsData().observe(this, Observer {
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCES -> it.data?.let { post -> callbacks(post) }
                Status.ERROR -> toasShort(it.message.toString())
            }
        })
    }

    fun getCommentList(callbacks: (List<CommentModel>) -> Unit){
        newsViewModel.getCommentData().observe(this, Observer {
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCES -> {
                    showLoading(false)
                    it.data.let { list ->
                        callbacks(list?.toCollection(ArrayList()) ?: emptyList())
                    }
                }
                Status.ERROR -> {
                    toasShort(it.message.toString())
                    showLoading(false)
                }
            }
        })
    }

    fun showDetail(news: PostModel?){
        binding.tvTitleDetail.text = news?.title
        binding.tvUser.text = String.format(getString(R.string.label_writer), userName)
        binding.contentDetail.text = news?.body
    }

    fun showListComment(listComment: List<CommentModel>){
        commentAdapter = CommentAdapter(listComment.toCollection(ArrayList()))
        binding.rvComment.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)
        }
    }

    private fun showLoading(show: Boolean){
        binding.swrDetail.isRefreshing = show
    }
}