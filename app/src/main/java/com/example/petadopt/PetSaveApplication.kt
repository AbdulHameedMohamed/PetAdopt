package com.example.petadopt

import androidx.multidex.MultiDexApplication
import com.example.petadopt.logging.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetSaveApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.init()
    }
}