package com.flowbiz.stacky

import android.app.Application
import com.flowbiz.stacky.di.AppComponentInitializer

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AppComponentInitializer.setApplication(this)
    }
}