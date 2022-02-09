package com.assessment.newspost.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assessment.newspost.R
import com.assessment.newspost.databinding.ItemNewsBinding
import com.assessment.newspost.model.PostModel
import com.assessment.newspost.model.UserModel

class NewsAdapter(private var data: ArrayList<PostModel> = arrayListOf(), private var listUser: ArrayList<UserModel> = arrayListOf(), val listener: NewsListener? = null) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(data[position], listUser[position], listener)
    }

    override fun getItemCount(): Int = data.size

    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(news: PostModel, user: UserModel ,listener: NewsListener?) = with(itemView){
            val binding = ItemNewsBinding.bind(itemView)
            binding.newsTitle.text = news.title?.uppercase()
            binding.newsDetail.text = news.body

            binding.newsUser.text = user.name
            binding.userCompany.text = user.company?.name
            setOnClickListener{
                listener?.onClick(news, user)
            }
        }
    }

    interface NewsListener{
        fun onClick(news: PostModel, user: UserModel)
    }
}