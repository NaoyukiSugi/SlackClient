package com.example.naoyukisugi.slackclient

import com.google.gson.annotations.SerializedName

data class ChannelHistory(@SerializedName("messages") val messages: List<Message>)