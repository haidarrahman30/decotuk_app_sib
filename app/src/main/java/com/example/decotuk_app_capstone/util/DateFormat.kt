package com.example.decotuk_app_capstone.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    fun setDate() : String {
        val date = SimpleDateFormat("dd LLL yyyy HH:mm:ss")
        return date.format(Date())
    }


}