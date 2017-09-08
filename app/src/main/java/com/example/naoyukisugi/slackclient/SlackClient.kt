package com.example.naoyukisugi.slackclient

import com.facebook.stetho.okhttp3.StethoInterceptor
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class SlackClient {

    fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }

    fun history(): Observable<ChannelHistory> {
        val retrofit = Retrofit.Builder()
                .client(createOkHttpClient())
                .baseUrl("https://slack.com/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(SlackApi::class.java)
        return api.history(
                token = "xoxp-235445599280-237071487205-238240391350-562207ba27f3caea4162c75ff6dc5708",
                channel = "C6Y0L4SRH")

    }

    fun postMessage(text: String): Observable<Message> {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://slack.com/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(SlackApi::class.java)
        return api.postMessage(
                token = "xoxp-235445599280-237071487205-238240391350-562207ba27f3caea4162c75ff6dc5708",
                channel = "C6Y0L4SRH",
                text = text,
                username = "Android")
    }

    fun postImage(file: MultipartBody.Part): Observable<PostImage> {
        val retrofit = Retrofit.Builder()
                .client(createOkHttpClient())
                .baseUrl("https://slack.com/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(SlackApi::class.java)

        val token = RequestBody.create(MultipartBody.FORM, "xoxp-235445599280-237071487205-238240391350-562207ba27f3caea4162c75ff6dc5708")
        val channel = RequestBody.create(MultipartBody.FORM, "C6Y0L4SRH")

        return api.postImage(
                token = token,
                channels = channel,
                file = file)
    }

    interface SlackApi {
        @FormUrlEncoded
        @POST("channels.history")
        fun history(@Field("token") token: String, @Field("channel") channel: String): io.reactivex.Observable<ChannelHistory>

        @FormUrlEncoded
        @POST("chat.postMessage")
        fun postMessage(@Field("token") token: String,
                        @Field("channel") channel: String,
                        @Field("text") text: String,
                        @Field("as_user") as_user: String = "false",
                        @Field("username") username: String): io.reactivex.Observable<Message>

        @Multipart
        @POST("files.upload")
        fun postImage(@Part("token") token: RequestBody,
                      @Part("channels") channels: RequestBody,
                      @Part file: MultipartBody.Part) : io.reactivex.Observable<PostImage>

    }


}