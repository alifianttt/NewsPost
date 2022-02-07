package com.assessment.newspost.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.assessment.newspost.databinding.ActivityPhotoDetailBinding
import com.assessment.newspost.model.PhotoModel
import com.assessment.newspost.utils.Status
import com.assessment.newspost.utils.toasShort
import com.assessment.newspost.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

class PhotoDetail : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) : Intent{
            return Intent(context, PhotoDetail::class.java)
        }
    }
    private lateinit var binding: ActivityPhotoDetailBinding
    private lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        getPhotoData { photoModel ->
            setPhoto(photoModel)
        }
    }

    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        viewModel.getPhotoDetail(id = 1)
    }

    private fun getPhotoData(callbacks:(PhotoModel) -> Unit){
        viewModel.getPhoto().observe(this, Observer {
            when(it.status){
                Status.LOADING -> toasShort("Loading")
                Status.SUCCES -> it.data?.let { photo ->
                    callbacks(photo)
                }
                Status.ERROR -> toasShort("Terjadi Kesalahan")
            }
        })
    }

    private fun setPhoto(photoModel: PhotoModel){
        Glide.with(applicationContext)
            .load(photoModel.url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(binding.imageView)

        binding.tvPhotoTitle.text = photoModel.title
    }
}