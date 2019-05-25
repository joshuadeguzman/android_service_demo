package io.jmdg.android_service_demo

import android.app.*
import android.app.Notification.PRIORITY_MIN
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context


//
// Created by Joshua de Guzman on 25/05/2019.
// https://github.com/joshuadeguzman
// https://jmdg.io
//

class MediaPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var TAG = MediaPlayerService::class.java.simpleName
    private var mMediaPlayer: MediaPlayer? = null

    val CHANNEL_ID = "FOREGROUND_CHANNEL_ID_ENERGYFM"
    val CHANNEL_NAME = "FOREGROUND_CHANNEL_NAME_ENERGYFM"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "On Create...")
        val uri = Uri.parse("android.resource://$packageName/raw/sample")
        mMediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            setOnPreparedListener(this@MediaPlayerService)
            setOnCompletionListener(this@MediaPlayerService)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "On Start Command...")

        val rootIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), rootIntent, 0)

        // Prepare media player
        mMediaPlayer?.prepareAsync()

        // Setup notification
        var notification: Notification? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Build notification channel if applicable (> Android O)
            // Create notification channel only for > Android O
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)

            notification = NotificationCompat
                    .Builder(this, CHANNEL_ID)
                    .setContentTitle("Android Service Demo")
                    .setContentText("This is a test")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build()
        } else {
            // Build notification
            notification = NotificationCompat
                    .Builder(this)
                    .setContentTitle("Android Service Demo")
                    .setContentText("This is a test")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build()
        }

        // Start service
        startForeground(1, notification)

        // This is suitable for media players (or similar services) that are not executing commands
        // but are running indefinitely and waiting for a job.
        return START_STICKY
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "Prepared...")
        mp?.start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d(TAG, "Completed...")
        mp?.let {
            if (it.isPlaying) {
                it.stop()
            }

            it.release()
        }
        stopSelf()
    }
}