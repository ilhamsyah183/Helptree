package com.dicoding.picodiploma.helptree.ui.chatbot

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ResponseHelper {

    private lateinit var hasil:String
    fun getResponseFromApi(message:String){
        val url = "https://arik.my.id/helptree/bot?query=$message"
        val client = AsyncHttpClient()
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val respon = responseObject.getString("response")
                    hasil = respon.toString()
                }catch (e: java.lang.Exception){
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getHasil(): String {
        return hasil
    }
}