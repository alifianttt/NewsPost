package com.assessment.newspost.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assessment.newspost.R
import com.assessment.newspost.databinding.ItemPhotoBinding
import com.assessment.newspost.model.PhotoModel
import com.assessment.newspost.utils.Constants
import com.assessment.newspost.utils.getUrl
import com.bumptech.glide.Glide

import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl





class PhotoAdapter(val dataPhoto: ArrayList<PhotoModel>,  val listener: PhotoListener? = null) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(dataPhoto[position], listener)
    }

    override fun getItemCount(): Int = dataPhoto.size

    class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(photoModel: PhotoModel, listener: PhotoListener?) = with(itemView){
            val binding = ItemPhotoBinding.bind(itemView)
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.img_place)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).load(getUrl(photoModel.thumbnailUrl.toString())).apply(options).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).centerCrop().into(binding.photoData)
            binding.photoData.setOnClickListener {
                listener?.onClick(photoModel)
            }
        }
    }

    interface PhotoListener{
        fun onClick(photoModel: PhotoModel)
    }
}