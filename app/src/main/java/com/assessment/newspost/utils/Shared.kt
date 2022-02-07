package com.assessment.newspost.utils

import android.app.Activity
import android.view.View
import android.widget.Toast

fun Activity.toasShort(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.setVisible(visible: Boolean){
    visibility = if (visible) View.VISIBLE else View.GONE
}