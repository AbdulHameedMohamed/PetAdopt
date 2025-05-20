package com.example.petadopt

import android.app.Application
import com.example.logging.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetSaveApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.init()
    }
}