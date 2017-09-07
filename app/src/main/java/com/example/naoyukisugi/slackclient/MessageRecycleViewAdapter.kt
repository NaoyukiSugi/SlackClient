package com.example.naoyukisugi.slackclient

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MessageRecycleViewAdapter(private val list: List<Message>) : RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        Log.d("onCreateViewHolder", "piyo")
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MessageViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        Log.d("onBindViewHolder", "piyo")
        holder.textView.text = list[position].text

    }

        override fun getItemCount(): Int = list.size

}