package com.flowbiz.stacky.di

import android.app.Application
import com.flowbiz.stacky.di.module.StorageModule

object AppComponentInitializer {
    private lateinit var appComponent: AppComponent
    private lateinit var application: Application

    fun setApplication(application: Application){
        this.application = application
    }

    fun getComponent(): AppComponent {
        if (!::appComponent.isInitialized){
            appComponent = DaggerAppComponent.builder().storageModule(StorageModule(application)).build()
        }
        return appComponent
    }
}