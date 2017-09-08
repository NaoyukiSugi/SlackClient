package com.example.naoyukisugi.slackclient

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.Image
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.facebook.stetho.Stetho
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainActivity : AppCompatActivity()  {
    private val REQUEST_CHOOSER: Int = 1000
    lateinit var m_uri: Uri

    var resultUri: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initialize(Stetho.newInitializerBuilder(application)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(application))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(application))
                .build())

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

        client.userList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
//                    it.profile

                }

        send_image_button.setOnClickListener {

            val cursor = contentResolver.query(resultUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
            //TODO ファイルのパスを直書きしてるので、選べるようにする
            cursor?.let {
//                var path: String? = null
//                if (cursor.moveToFirst()) {
//                    path = cursor.getString(0)
//                }
//                cursor.close()
//                var file: File? = null
//                path?.let {
//                    file = File(path)
//                }
                val file = File("/mnt/sdcard/Download/images.jpg")

                val requestFile: RequestBody = RequestBody.create(
                        MediaType.parse(contentResolver.getType(resultUri)),
                        file
                )

                var body: MultipartBody.Part = MultipartBody.Part.createFormData("file",
                        file!!.name , requestFile)

                client.postImage(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            Log.d("postImage", it.isSuccess.toString())

                        }
            }
        }



        media_button.setOnClickListener {
            var intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
            intentGallery.setType("image/jpeg")
            startActivityForResult(intentGallery, REQUEST_CHOOSER)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != Activity.RESULT_OK) {
                // キャンセル時
                return
            }

            //選んだ画像のUriを取得
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


//    private fun showGallery() {
//        val intentGallery: Intent
//        intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
//        intentGallery.setType("image/jpeg")
//        startActivityForResult(intentGallery, REQUEST_CHOOSER)
//    }

}