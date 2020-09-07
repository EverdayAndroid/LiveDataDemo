package com.example.livedatademo

import android.app.Application

class App:Application() {
    companion object{
        private lateinit var application: Application
        fun getInstance():Application{
            return application
        }
    }


    override fun onCreate() {
        super.onCreate()
        application = this
    }
}