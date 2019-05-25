package io.jmdg.android_service_demo

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.java.simpleName
    private var mediaPlayerServiceIntent : Intent? = null
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Play local media
        // this.playRawLocalMediaSource()
        // this.playUriLocalMediaSource()

        // Subscribe to button clicks
        btPlay.setOnClickListener {
            // Must keep reference with the service
            if (isPlaying) {
                stopMediaSourceFromService()
            } else {
                playMediaSourceFromService()
            }

            this.isPlaying = !isPlaying
            Log.d(TAG, isPlaying.toString())
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
        mediaPlayerServiceIntent = Intent(this, MediaPlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mediaPlayerServiceIntent)
        } else {
            startService(mediaPlayerServiceIntent)
        }
    }

    private fun stopMediaSourceFromService() {
        mediaPlayerServiceIntent?.let {
            stopService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO
        // Reference mediaPlayers globally and release them here
    }
}
