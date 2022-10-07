package com.example.mathtips.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider

class MyApplication : Application() {
    companion object{
        lateinit var application: Application
        lateinit var factory : ViewModelProvider.AndroidViewModelFactory
    }

    override fun onCreate() {
        super.onCreate()
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        application = this
    }
}