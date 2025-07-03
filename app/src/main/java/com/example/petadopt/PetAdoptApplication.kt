package com.example.petadopt

import androidx.multidex.MultiDexApplication
import com.example.logging.domain.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetAdoptApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.init()
    }
}