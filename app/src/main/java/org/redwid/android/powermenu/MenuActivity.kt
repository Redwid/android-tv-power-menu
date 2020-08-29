package org.redwid.android.powermenu

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import eu.chainfire.libsuperuser.Shell
import kotlinx.android.synthetic.main.menu_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * https://thinkandroid.wordpress.com/2010/01/24/handling-screen-off-and-screen-on-intents/
 */
class MenuActivity: AppCompatActivity() {

    private val SHUTDOWN_BROADCAST = "am broadcast android.intent.action.ACTION_SHUTDOWN"
    private val POWER_OFF_CMD = "reboot -p"
    private val RESTART_CMD = "reboot"
    private val SLEEP_CMD = "input keyevent KEYCODE_SLEEP"
    private val RESTART_SOFT_REBOOT_CMD = "setprop ctl.restart zygote"
    private val RESTART_RECOVERY_CMD = "reboot recovery"
    private val RESTART_BOOTLOADER_CMD = "reboot bootloader"

    private val haManager: HaManager by lazy { HaManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        button_power_off.setOnClickListener {
            shell_run(POWER_OFF_CMD)
            haManager.notifyPowerOff(baseContext)}

        button_sleep.setOnClickListener {
            shell_run(SLEEP_CMD)
            haManager.notifySleep(baseContext)
        }

        button_restart.setOnClickListener { shell_run(RESTART_CMD) }
        button_restart.setOnLongClickListener {
            shell_run(RESTART_SOFT_REBOOT_CMD)
            true }

        button_settings.setOnClickListener {
            button_recovery.visibility = View.VISIBLE
            button_bootloader.visibility = View.VISIBLE
            it.visibility = View.GONE}

        button_recovery.setOnClickListener { shell_run(RESTART_RECOVERY_CMD) }
        button_bootloader.setOnClickListener { shell_run(RESTART_BOOTLOADER_CMD) }
    }

    private fun shell_run(cmd: String) {
        Log.d(LOG_TAG, "shell_run($cmd)")
        content.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            var time = System.currentTimeMillis()
            Shell.Pool.SU.run(SHUTDOWN_BROADCAST)
            Shell.Pool.SU.run(cmd)
            time = System.currentTimeMillis() - time
            if(time < 500) {
                delay(500 - time)
            }
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d(LOG_TAG, "onKeyDown(), keyCode: $keyCode, event: $event")
        return if (keyCode == KeyEvent.KEYCODE_POWER) {
            Log.d(LOG_TAG, "onKeyDown() KeyEvent.KEYCODE_POWER")
            return true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(LOG_TAG, "onKeyLongPress(), keyCode: $keyCode, event: $event")
        return if (keyCode == KeyEvent.KEYCODE_POWER) {
            Log.d(LOG_TAG, "onKeyLongPress() KeyEvent.KEYCODE_POWER")
            true
        } else super.onKeyLongPress(keyCode, event)
    }

    companion object {
        private val LOG_TAG = MenuActivity.javaClass.simpleName
    }
}