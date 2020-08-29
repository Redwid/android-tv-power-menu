package org.redwid.android.powermenu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class ScreenOnBroadcastReceiver: BroadcastReceiver() {

    private val haManager: HaManager by lazy { HaManager() }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(LOG_TAG, "onReceive(), intent.action: ${intent?.action}")
    }

    companion object {
        val LOG_TAG = ScreenOnBroadcastReceiver::class.java.simpleName
    }
}