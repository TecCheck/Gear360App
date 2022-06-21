package io.github.teccheck.gear360app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.github.teccheck.gear360app.R

class ExoplayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer)

        val playerView = findViewById<StyledPlayerView>(R.id.player_view)
        val player = ExoPlayer.Builder(this).build()

        playerView.player = player

        player.addMediaItem(MediaItem.fromUri("http://192.168.107.1:7679/livestream_high.avi"))
        player.prepare()
        player.play()
    }
}