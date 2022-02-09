package com.assessment.newspost.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.newspost.R
import com.assessment.newspost.adapter.AlbumAdapter
import com.assessment.newspost.adapter.PhotoAdapter
import com.assessment.newspost.databinding.ActivityUserBinding
import com.assessment.newspost.model.AlbumModel
import com.assessment.newspost.model.PhotoModel
import com.assessment.newspost.model.UserModel
import com.assessment.newspost.utils.Status
import com.assessment.newspost.utils.setVisible
import com.assessment.newspost.utils.toasShort
import com.assessment.newspost.viewmodel.NewsViewModel

class UserActivity : AppCompatActivity() {
    companion object{
        const val TYPE_KEY = "key"
        const val ID_KEY = "id"
        fun newIntent(context: Context, id : Int = 0) : Intent {
            return Intent(context, UserActivity::class.java)
                .putExtra(ID_KEY, id)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        }
    }

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityUserBinding
    private var isEditType = false
    private var idUser = 0
    private lateinit var albumAdapter: AlbumAdapter
    private var listAlbum = ArrayList<AlbumModel>()
    private var listPhoto = ArrayList<PhotoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        albumAdapter = AlbumAdapter(listAlbum, listPhoto,this)
        setContentView(binding.root)
        setupViewModel()
    }

    private val listener = object : PhotoAdapter.PhotoListener{
        override fun onClick(photoModel: PhotoModel) {
            startActivity(PhotoDetail.newIntent(this@UserActivity, photoModel.id ?: 0))
        }

    }
    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        isEditType = intent.getBooleanExtra(TYPE_KEY, false)
        idUser = intent.getIntExtra(ID_KEY, 0)
        viewModel.userDetail(idUser)
        viewModel.getAlbumList(idUser)
        viewModel.getPhotoList(idUser)

        binding.swrPhoto.setOnRefreshListener {
            viewModel.userDetail(idUser)
            viewModel.getAlbumList(idUser)
            viewModel.getPhotoList(idUser)
            listAlbum.clear()
            listPhoto.clear()
        }

        getUserData { userModel ->
            setupView(userModel)
        }

        getAlbumList {
            listAlbum.addAll(it)
            setupAlbumList(listAlbum)
        }
        getPhotoList {
            listPhoto.addAll(it)
            setupAlbumList(listAlbum, listPhoto)
            albumAdapter.notifyDataSetChanged()
        }




    }

    private fun getUserData(callbacks:(UserModel) -> Unit){
        viewModel.getUser().observe(this, Observer {
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCES -> {
                    showLoading(false)
                    it.data?.let { user ->
                        callbacks(user)
                    }
                }
                Status.ERROR -> {
                    toasShort(it.message.toString())
                    showLoading(false)
                }
            }
        })
    }

    private fun getAlbumList(callbacks: (List<AlbumModel>) -> Unit){
        viewModel.getAlbumData().observe(this, Observer {
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCES -> {
                    listAlbum.clear()
                    it.data?.let { album ->
                        callbacks(album)
                    }
                }
                Status.ERROR -> showLoading(false)
            }
        })
    }

    private fun getPhotoList(callbacks: (List<PhotoModel>) -> Unit){
        viewModel.getPhotoData().observe(this, Observer {
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCES -> {
                    listPhoto.clear()
                    showLoading(false)
                    it.data?.let { photo ->
                        callbacks(photo)
                    }
                }
                Status.ERROR -> showLoading(false)
            }
        })
    }

    private fun setupView(userModel: UserModel){
        binding.fieldName.tvLabel.text = getString(R.string.name_label)
        binding.fieldName.tvValue.text = userModel.name

        binding.fieldEmail.tvLabel.text = getString(R.string.email_label)
        binding.fieldEmail.tvValue.text = userModel.email

        binding.fieldAddress.tvLabel.text = getString(R.string.address_label)
        binding.fieldAddress.tvValue.text = String.format(getString(R.string.address_format), userModel.address?.street, userModel.address?.city)

        binding.fieldCompany.tvLabel.text = getString(R.string.company_label)
        binding.fieldCompany.tvValue.text = userModel.company?.name

    }

    private fun setupAlbumList(album: List<AlbumModel>, listPhoto: List<PhotoModel> = listOf()){
        if (album.isNotEmpty() && listPhoto.isNotEmpty()){
            albumAdapter = AlbumAdapter(album.toCollection(ArrayList()), listPhoto.toCollection(ArrayList()), this, listener)
            binding.rvPhoto.apply {
                adapter = albumAdapter
                layoutManager = LinearLayoutManager(this@UserActivity)
            }
            albumAdapter.notifyDataSetChanged()
        }

        Log.d("view Photo", listPhoto.size.toString())
    }

    private fun showLoading(show: Boolean){
        //binding.pbNews.setVisible(show)
        binding.swrPhoto.isRefreshing = show
    }

}