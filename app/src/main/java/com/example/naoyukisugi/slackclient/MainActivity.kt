package com.example.naoyukisugi.slackclient

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.Util
import java.io.File

class MainActivity : AppCompatActivity()  {
    private val REQUEST_CHOOSER: Int = 1000
    lateinit var m_uri: Uri

    var resultUri: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv: RecyclerView = findViewById(R.id.messageRecycleView) as RecyclerView

        val client = SlackClient()

        val llm: LinearLayoutManager = LinearLayoutManager(this)

        val editText: EditText = findViewById(R.id.edit_text) as EditText

        val send_button: Button = findViewById(R.id.send_button) as Button

        val send_image_button: Button = findViewById(R.id.send_image_button) as Button

        val media_button: Button = findViewById(R.id.media_button) as Button

        llm.reverseLayout = true
//        llm.stackFromEnd = true

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

        send_image_button.setOnClickListener {

            var file = File(resultUri.path)
//            var file = File(Environment.DIRECTORY_DOWNLOADS + "/" + "images.jpg")
//            var file = File(Environment.DIRECTORY_DOCUMENTS + "/" + "images.jpg")

//            val requestFile: RequestBody = RequestBody.create(
//                    MediaType.parse(contentResolver.getType(resultUri)),
//                    file
//            )

            val requestFile: RequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"), file
            )

            var body: MultipartBody.Part = MultipartBody.Part.createFormData("file",
                    file.getName(), requestFile)

//             var body: MultipartBody.Part = MultipartBody.Part.createFormData("file",
//                    "images.jpg", requestFile)


            client.postImage(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        Log.d("test", resultUri.path)
                    }
        }



        media_button.setOnClickListener {
            var intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
            intentGallery.setType("image/jpeg")
            startActivityForResult(intentGallery, REQUEST_CHOOSER)

        }

//        var photoName: String = System.currentTimeMillis().toString() + ".jpg"
//        var contentValues: ContentValues = ContentValues()
//        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
//        m_uri = getContentResolver()
//                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != Activity.RESULT_OK) {
                // キャンセル時
                return
            }

            resultUri = (if (data != null) data.data else m_uri) ?: return
            MediaScannerConnection.scanFile(
                    this,
                    arrayOf(resultUri.path),
                    arrayOf("image/jpeg"),
                    null)
            // 画像を設定
            val imageView = findViewById(R.id.imageView) as ImageView
            imageView.setImageURI(resultUri)
        }

    }

    private fun showGallery() {
        val intentGallery: Intent
        intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
        intentGallery.setType("image/jpeg")
        startActivityForResult(intentGallery, REQUEST_CHOOSER)
    }


}