package com.assessment.newspost.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

fun Activity.toasShort(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.setVisible(visible: Boolean){
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun getUrl(url: String): GlideUrl {
    return GlideUrl(
        url, LazyHeaders.Builder()
            .addHeader("User-Agent", Constants.CONFIG.USER_AGENT)
            .build()
    )
}