package com.example.naoyukisugi.slackclient

import com.google.gson.annotations.SerializedName

data class Message(@SerializedName("text") val text: String,
                   @SerializedName("user") val user: User)