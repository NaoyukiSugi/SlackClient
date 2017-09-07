package com.example.naoyukisugi.slackclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv: RecyclerView = findViewById(R.id.messageRecycleView) as RecyclerView

        val client = SlackClient()

        val llm: LinearLayoutManager = LinearLayoutManager(this)

        val editText: EditText = findViewById(R.id.edit_text) as EditText

        val send_button: Button = findViewById(R.id.send_button) as Button

        //チャンネル内のメッセージ一覧表示
        client.history()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    rv.setHasFixedSize(true)
                    rv.layoutManager = llm
                    rv.adapter = MessageRecycleViewAdapter(it.messages)
                }

        send_button.setOnClickListener {
            client.postMessage(editText.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{}

            editText.editableText.clear()
        }

    }

}