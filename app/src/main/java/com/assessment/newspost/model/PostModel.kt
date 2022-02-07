package com.assessment.newspost.model

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostModel(
    var body: String? = "",
    var userId: Int? = 0,
    var id: Int? = 0,
    var title: String? = "",
) : Parcelable