package com.example.naoyukisugi.slackclient

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView


class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView
    val userIconView: ImageView

    init {
        textView = itemView.findViewById(R.id.textView)
        userIconView = itemView.findViewById(R.id.userIconView)
    }

}