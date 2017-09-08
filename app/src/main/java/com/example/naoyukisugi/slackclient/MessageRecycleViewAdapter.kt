package com.example.naoyukisugi.slackclient

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

class MessageRecycleViewAdapter(private val list: List<Message_2>) : RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        Log.d("onCreateViewHolder", "piyo")
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MessageViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//        holder.textView.text = list[position].text
        holder.userIconView.setImageBitmap(list[position].bitmap)


        // TODO urlからBitmaに変換
        Glide.with(holder.userIconView.context)
                .load(list[position].image_24)
                .placeholder(R.drawable.abc_ic_menu_copy_mtrl_am_alpha)
                .into(holder.userIconView)
    }

    override fun getItemCount(): Int = list.size


}