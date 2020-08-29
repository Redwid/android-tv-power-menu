package org.redwid.android.powermenu.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.redwid.android.powermenu.ha.HaManager


class BootBroadcastReceiver: BroadcastReceiver() {

    private val haManager: HaManager by lazy { HaManager() }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(LOG_TAG, "onReceive(), action: ${intent?.action}")
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED &&
            intent?.action != Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            return
        }
        haManager.notifyBootCompleted(context)
    }

    companion object {
        private val LOG_TAG = BootBroadcastReceiver::class.java.simpleName
    }
}