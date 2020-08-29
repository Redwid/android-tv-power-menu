package org.redwid.android.powermenu

import android.app.Application

class PowerMenuApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        UpdateService.createAndStart(applicationContext)
    }
}