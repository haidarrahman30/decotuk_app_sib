package com.example.decotuk_app_capstone.data.preferences

class PreferenceRepository (private val preference : UserPreference) {
    companion object{
        @Volatile
        private var INSTANCE : PreferenceRepository? = null

        fun getInstance(sesi : UserPreference) : PreferenceRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceRepository(sesi)
            }
        }

    }

    fun loginUser(key : String, value : String){
        preference.createLoginSession()
        preference.saveToPreference(key, value)
    }

    fun getUser(key : String) = preference.getFromPreference(key)

    fun isUserLogin() = preference.isLogin

    fun logoutUser() = preference.logOut()
}