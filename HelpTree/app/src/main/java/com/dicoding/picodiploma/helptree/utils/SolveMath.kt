package com.dicoding.picodiploma.helptree.utils

import android.util.Log

object SolveMath {

    fun solveMath(equation: String) : Int{

        val newEquation2 = equation.replace(" ", "")

        val regex = Regex("[^-+*/0-9]")
        val newEquation = regex.replace(newEquation2, "")
        Log.d("Math", newEquation)

        return when {
            newEquation.contains("+") -> {
                val split = newEquation.split("+")
                val result = split[0].toInt() + split[1].toInt()
                result
            }
            newEquation.contains("-") -> {
                val split = newEquation.split("-")
                val result = split[0].toInt() - split[1].toInt()
                result
            }
            newEquation.contains("*") -> {
                val split = newEquation.split("*")
                val result = split[0].toInt() * split[1].toInt()
                result
            }
            newEquation.contains("/") -> {
                val split = newEquation.split("/")
                val result = split[0].toInt() / split[1].toInt()
                result
            }
            else -> {
                0
            }
        }
    }
}