package com.dicoding.picodiploma.helptree.utils

import android.util.Log
import com.dicoding.picodiploma.helptree.utils.Constants.BOT_HELPTREE
import com.dicoding.picodiploma.helptree.utils.Constants.OPEN_GOOGLE
import com.dicoding.picodiploma.helptree.utils.Constants.SEARCH_GOOGLE
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*

object BotResponse {

    lateinit var akhir: String
    fun standarResponse(_message:String):String{
        val random = (0..2).random()
        val message = _message.toLowerCase(Locale.ROOT)
        return when{
            message.contains("aku baik")->{
                when(random){
                    0-> "Syukurlah..."
                    1-> "Bagus...."
                    2-> "Hebat..."
                    else -> "error"
                }
            }


            message.contains("hitung") ->{
                val equation = message.substringAfter("hitung")
                return try {
                    val answer = SolveMath.solveMath(equation)
                    answer.toString()
                }catch (e: Exception){
                    "Maaf aku tidak bisa menghitungnya..."
                }
            }

            message.contains("lempar") && message.contains("koin") ->{
                val rand= (0..1).random()
                val result = if(rand == 0)"Kepala" else "Ekor"

                "Aku sudah melempar Koinnya dan hasilnya adalah $result"
            }

            message.contains("Error C01404") ->{
                val rand= (0..1).random()
                val result = if(rand == 0)"Kami sedang dalam perbaikan" else "Kami akan segera hadir kembali maaf"

                "Oops... $result"
            }

            message.contains("time") && message.contains("?") ->{
                Time.timeStamp()
            }

            message.contains("buka") && message.contains("google") ->{
                OPEN_GOOGLE
            }

            message.contains("cari") ->{
                SEARCH_GOOGLE
            }

            else ->{
                BOT_HELPTREE
            }
        }
    }

    private fun getResponseFromApi(message:String){
        val url = "https://arik.my.id/helptree/bot?query=$message"
        val client = AsyncHttpClient()
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    akhir = responseObject.getString("response")
                }catch (e: java.lang.Exception){
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }
}