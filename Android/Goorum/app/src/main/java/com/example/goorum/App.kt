package com.example.goorum

import android.app.Application
import com.example.goorum.my.MySharedPreferences

class App: Application() {
    companion object {
        lateinit var prefs: MySharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}