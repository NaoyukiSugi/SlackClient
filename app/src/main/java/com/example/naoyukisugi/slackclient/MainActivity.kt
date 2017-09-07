package com.example.naoyukisugi.slackclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv: RecyclerView = findViewById(R.id.messageRecycleView) as RecyclerView

        val client = SlackClient()

        val llm: LinearLayoutManager = LinearLayoutManager(this)

        client.history()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    rv.setHasFixedSize(true)
                    rv.layoutManager = llm
                    rv.adapter = MessageRecycleViewAdapter(it.messages)
                }

    }

}