package com.example.mathtips.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.mathtips.app.MyApplication
import com.sangtb.androidlibrary.utils.SharePrefs

class SharePrefsIplm : SharePrefs() {
    override val sharedPref: SharedPreferences by lazy {
        MyApplication.application.getSharedPreferences("PREF", MODE_PRIVATE)
    }
    override val editor: SharedPreferences.Editor by lazy { sharedPref.edit() }

    fun getTotalAnswer(key : String) = sharedPref.getInt(key,0)

    fun getTotalScoreFromIdAndLevel(key : String) = sharedPref.getInt(key,0)

    fun getRateScoreFromIdAndLevel(key : String) = sharedPref.getInt(key,0)
}