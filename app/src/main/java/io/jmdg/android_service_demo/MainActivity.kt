package io.jmdg.android_service_demo

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Play local media
        this.playLocalMediaSource()
    }

    private fun playLocalMediaSource() {
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.sample)
        mediaPlayer?.start()
    }
}
