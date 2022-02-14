package com.akshay_taru.package_library.entites

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val versionCode: Long,
    val versionName: String
)
