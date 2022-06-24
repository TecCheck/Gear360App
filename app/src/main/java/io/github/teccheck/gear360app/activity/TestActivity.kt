package io.github.teccheck.gear360app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import io.github.teccheck.gear360app.live.LiveTestActivity
import io.github.teccheck.gear360app.R

private const val TAG = "TestActivity"

class TestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setupBackButton()

        findViewById<Button>(R.id.btn_live).setOnClickListener {
            startActivity(Intent(this, LiveTestActivity::class.java))
        }

        findViewById<Button>(R.id.btn_exoplayer).setOnClickListener {
            startActivity(Intent(this, ExoplayerActivity::class.java))
        }
    }
}