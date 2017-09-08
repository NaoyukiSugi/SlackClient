package com.example.naoyukisugi.slackclient

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Message_2(@SerializedName("text") val text: String,
                     @SerializedName("user") val user: User,
                     @SerializedName("image_24") val image_24: String,
                     @SerializedName("bitmap") val bitmap: Bitmap)