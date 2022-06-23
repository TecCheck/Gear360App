package io.github.teccheck.gear360app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.player.JStreamExtractor


class ExoplayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer)

        val playerView = findViewById<PlayerView>(R.id.player_view)

        //val surface = (playerView.videoSurfaceView as SphericalGLSurfaceView)

        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val dataSourceFactory: DataSource.Factory = DataSource.Factory {
            val dataSource: HttpDataSource = httpDataSourceFactory.createDataSource()
            dataSource.setRequestProperty("User-Agent", "Android Linux")
            dataSource
        }

        val mediaSourceFactory = DefaultMediaSourceFactory(this, JStreamExtractor.Factory())
            .setLiveMaxSpeed(1.02f)
            .setDataSourceFactory(dataSourceFactory)

        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(
                200, 1000,
                100, 100
            )
            .setBackBuffer(200, false)
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl()

        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .setLoadControl(loadControl)
            .build()

        playerView.player = player

        //player.addAnalyticsListener(EventLogger());
        player.addMediaItem(MediaItem.fromUri("http://192.168.107.1:7679/livestream_high.avi"))
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        player.stop()
        player.release()
        super.onDestroy()
    }
}