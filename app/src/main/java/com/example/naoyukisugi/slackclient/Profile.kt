package com.example.naoyukisugi.slackclient

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("id") val id: String,
                   @SerializedName("image_24") val image_24: String)