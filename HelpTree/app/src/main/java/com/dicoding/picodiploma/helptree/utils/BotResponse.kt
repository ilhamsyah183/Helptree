package com.dicoding.picodiploma.helptree.utils

import com.dicoding.picodiploma.helptree.utils.Constants.BOT_HELPTREE
import com.dicoding.picodiploma.helptree.utils.Constants.OPEN_GOOGLE
import com.dicoding.picodiploma.helptree.utils.Constants.SEARCH_GOOGLE
import java.util.*

object BotResponse {

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

}