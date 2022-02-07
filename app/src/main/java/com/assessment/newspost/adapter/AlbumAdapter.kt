package com.assessment.newspost.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assessment.newspost.R
import com.assessment.newspost.databinding.ItemAlbumBinding
import com.assessment.newspost.model.AlbumModel
import com.assessment.newspost.model.PhotoModel

class AlbumAdapter(private var dataAlbum: ArrayList<AlbumModel> = arrayListOf(), private var urlPhoto: ArrayList<PhotoModel> = arrayListOf(), val mContext: Context, val listener: PhotoAdapter.PhotoListener? = null ) : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        return AlbumHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(dataAlbum[position], urlPhoto, mContext, listener)
    }

    override fun getItemCount(): Int = dataAlbum.size

    class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(albumModel: AlbumModel, photoModel: ArrayList<PhotoModel>, context: Context, listener: PhotoAdapter.PhotoListener? = null) = with(itemView){
            val binding = ItemAlbumBinding.bind(itemView)
            val adapterPhoto = PhotoAdapter(photoModel, listener)
            binding.albumTitle.text = albumModel.title
            binding.rvListPhoto.apply {
                adapter = adapterPhoto
                layoutManager = /*LinearLayoutManager(context)*/ GridLayoutManager(context, 4)
            }
        }
    }


}