package com.example.decotuk_app_capstone.data.preferences

import android.content.Context

class UserPreference(context : Context) {
    companion object{
        const val KEY_LOGIN = "key_login"
    }

    private val pref = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun createLoginSession(){
        editor.putBoolean(KEY_LOGIN, true).commit()
    }

    fun logOut(){
        editor.clear().commit()
    }

    val isLogin = pref.getBoolean(KEY_LOGIN, false)

    fun saveToPreference(key : String, value : String ) {
        editor.putString(key, value).commit()
    }

    fun getFromPreference(key : String) = pref.getString(key, "")



}