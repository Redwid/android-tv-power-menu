package org.redwid.android.powermenu.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import org.redwid.android.powermenu.ha.HaManager

class ScreenStatusReceiver: BroadcastReceiver() {

    private val haManager: HaManager by lazy { HaManager() }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(LOG_TAG, "onReceive($context, $intent)")
        if (Intent.ACTION_SCREEN_ON == intent?.action) {
            Log.d(LOG_TAG, "ACTION_SCREEN_ON")
            haManager.notifyWakeUp(context)
        } else if (Intent.ACTION_SCREEN_OFF == intent?.action) {
            Log.d(LOG_TAG, "ACTION_SCREEN_OFF")
            haManager.notifySleep(context)
        }
    }

    fun register(context: Context?) {
        Log.d(LOG_TAG, "register($context)")
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        context?.registerReceiver(this, filter)
    }

    companion object {
        private val LOG_TAG = ScreenStatusReceiver::class.java.simpleName
    }
}