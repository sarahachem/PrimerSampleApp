package com.example.primerapplication.authentication

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson

class PrefsToken(context: Context) {

    companion object {
        private const val PREFS_KEY_TOKEN = "token"
        private const val PREFS_NAME = "tokenPrefs"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(): TokenResponse? {
        return Gson().fromJson(prefs.getString(PREFS_KEY_TOKEN, null), TokenResponse::class.java)
    }

    fun saveToken(token: TokenResponse) {
        prefs.edit { putString(PREFS_KEY_TOKEN, Gson().toJson(token)) }
    }

    fun clear() {
        prefs.edit { clear() }
    }
}
