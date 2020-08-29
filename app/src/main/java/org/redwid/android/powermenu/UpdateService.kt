package org.redwid.android.powermenu

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import org.redwid.android.powermenu.receivers.ScreenStatusReceiver


class UpdateService: Service() {

    private val screenStatusReceiver = ScreenStatusReceiver()
    private var notificationManager: NotificationManager? = null

    private val binder: IBinder = LocalBinder()

    class LocalBinder : Binder() {
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startForegroundIfNeeded()

        screenStatusReceiver.register(applicationContext)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand($intent, $flags, $startId)")
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(LOG_TAG, "onTaskRemoved")
//        val restartServiceIntent = Intent(applicationContext, this.javaClass)
//        restartServiceIntent.setPackage(packageName)
//        val restartServicePendingIntent = PendingIntent.getService(
//            applicationContext,
//            1,
//            restartServiceIntent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val alarmService =
//            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
//            restartServicePendingIntent
        super.onTaskRemoved(rootIntent)
    }

    fun getNotification(title: String?, text: String?): Notification? {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .build()
    }

    private fun startForegroundIfNeeded() {
        Log.d(LOG_TAG, "startForegroundIfNeeded()")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.vibrationPattern = longArrayOf(0L)
            channel.enableVibration(true)
            notificationManager?.createNotificationChannel(channel)
            startForeground(NOTIFICATION_ID, getNotification("", ""))
        }
    }


    companion object {

        fun createAndStart(context: Context?) {
            val serviceIntent = Intent(context, UpdateService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(LOG_TAG, "startForegroundService()")
                context?.startForegroundService(serviceIntent)
            } else {
                Log.d(LOG_TAG, "startService()")
                context?.startService(serviceIntent)
            }
        }

        const val NOTIFICATION_ID = 1212
        const val NOTIFICATION_CHANNEL_ID = "power-menu-service"
        private val LOG_TAG = UpdateService::class.java.simpleName
    }
}