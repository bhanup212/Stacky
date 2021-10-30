package com.flowbiz.stacky.di

import com.flowbiz.stacky.di.module.NetworkModule
import com.flowbiz.stacky.di.module.StorageModule
import com.flowbiz.stacky.di.module.ViewModelModule
import com.flowbiz.stacky.ui.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
}