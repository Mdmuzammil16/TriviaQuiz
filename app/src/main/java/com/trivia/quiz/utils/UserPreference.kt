package com.trivia.quiz.utils

import android.content.Context
import android.content.SharedPreferences
import com.trivia.quiz.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor (@ApplicationContext val context: Context) {

    fun saveUserinfo(key: String?, value: String?) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    fun getUserinfo(key: String?): String {
        return preferences.getString(key, "").toString()
    }

    fun clearAll() {
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

    companion object {
        lateinit var preferences: SharedPreferences
        var instance: UserPreference? = null
        fun getInstance(context: Context): UserPreference? {
            if (instance == null) {
                instance = UserPreference(context)
            }
            return instance
        }
    }

    init {
        preferences =
            context.getSharedPreferences(context.getString(R.string.store), Context.MODE_PRIVATE)
    }
}