package io.github.teccheck.gear360app.bluetooth2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

private const val TAG = "Gear360Service"

class Gear360Service : Service() {

    private val binder = LocalBinder()


    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        fun getService(): Gear360Service {
            return this@Gear360Service
        }
    }
}