package io.jmdg.android_service_demo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

//
// Created by Joshua de Guzman on 25/05/2019.
// https://github.com/joshuadeguzman
// https://jmdg.io
//

class BoundMediaPlayerService : Service() {

    private val mBinder = MediaServiceBinder()

    inner class MediaServiceBinder : Binder() {
        fun getService(): BoundMediaPlayerService {
            return this@BoundMediaPlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    fun getCurrentDateTime(): String {
        val format = SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US)
        return format.format(Date())
    }
}