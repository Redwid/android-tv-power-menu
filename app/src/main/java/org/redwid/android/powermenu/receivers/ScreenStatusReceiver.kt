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
        filter.addAction(HDMI_PLUG_ACTION)
        context?.registerReceiver(this, filter)
    }

    companion object {
        //Source: https://github.com/rock64-android/platform-packages-apps-TvSettings/blob/ca544f40bbb0290ad636c660e2091d7263073643/Settings/src/com/android/tv/settings/display/DisplayFragment.java
        const val HDMI_PLUG_ACTION = "android.intent.action.HDMI_PLUGGED"

        private val LOG_TAG = ScreenStatusReceiver::class.java.simpleName
    }
}