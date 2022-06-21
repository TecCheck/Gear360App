package io.github.teccheck.gear360app.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.teccheck.gear360app.live.LiveTestActivity
import io.github.teccheck.gear360app.R

private const val TAG = "TestActivity"

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btn_live).setOnClickListener {
            startActivity(Intent(this, LiveTestActivity::class.java))
        }

        findViewById<Button>(R.id.btn_exoplayer).setOnClickListener {
            startActivity(Intent(this, ExoplayerActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }
}