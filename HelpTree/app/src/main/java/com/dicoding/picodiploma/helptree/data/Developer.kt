package com.dicoding.picodiploma.helptree.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Developer(
    var name:String?="",
    var photo:Int?=0,
    var path:String?=""
):Parcelable
