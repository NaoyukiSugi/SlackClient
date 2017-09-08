package com.example.naoyukisugi.slackclient

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") val id: String,
                @SerializedName("profile") val profile: List<String>)