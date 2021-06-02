package com.dicoding.picodiploma.helptree.utils

import android.annotation.SuppressLint
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Time {

    @SuppressLint("SimpleDateFormat")
    fun timeStamp():String{

        val timestamp = Timestamp(System.currentTimeMillis())
        val simpleDate = SimpleDateFormat("HH:mm")
        val times = simpleDate.format(Date(timestamp.time))

        return times.toString()
    }
}