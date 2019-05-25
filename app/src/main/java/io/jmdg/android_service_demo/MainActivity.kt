package io.jmdg.android_service_demo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.java.simpleName

    var boundMediaPlayerService : BoundMediaPlayerService? = null
    var isBound = false

    private val connection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "Service connected...")
            val binder = service as BoundMediaPlayerService.MediaServiceBinder
            boundMediaPlayerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "Service disconnected...")
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Bind bound service
        val intent = Intent(this, BoundMediaPlayerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        // Play local media
        // this.playRawLocalMediaSource()
        // this.playUriLocalMediaSource()

        // Subscribe to button clicks
        btPlay.setOnClickListener {
            playMediaSourceFromService()
        }

        btStop.setOnClickListener {
            stopMediaSourceFromService()
        }

        btRetrieveDate.setOnClickListener {
            boundMediaPlayerService?.let {
                tvDate.text = it.getCurrentDateTime()
            }
        }
    }

    private fun playRawLocalMediaSource() {
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.sample)
        mediaPlayer?.start()
    }

    private fun playUriLocalMediaSource() {
        val uri = Uri.parse("android.resource://$packageName/raw/sample")
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            prepare()
            start()
        }
    }

    private fun playMediaSourceFromService() {
        val mediaIntent = Intent(this, MediaPlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mediaIntent)
        } else {
            startService(intent)
        }
    }

    private fun stopMediaSourceFromService() {
        val mediaIntent = Intent(this, MediaPlayerService::class.java)
        stopService(mediaIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO
        // Reference mediaPlayers globally and release them here
    }
}
