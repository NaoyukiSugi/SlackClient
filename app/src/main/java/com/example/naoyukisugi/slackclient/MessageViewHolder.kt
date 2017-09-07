package com.example.naoyukisugi.slackclient

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView


class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView

    init {
        textView = itemView.findViewById(R.id.textView)
    }

}