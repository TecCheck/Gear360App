package io.github.teccheck.gear360app.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.bluetooth2.Gear360Service
import io.github.teccheck.gear360app.utils.SettingsHelper

private const val TAG = "ControlActivity"
const val EXTRA_MAC_ADDRESS = "mac_address"

class TestActivity : AppCompatActivity() {

    private var gear360Service: Gear360Service? = null

    private val gearServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            gear360Service = (service as Gear360Service.LocalBinder).getService()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            gear360Service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val settings = SettingsHelper(this)
        val editor = settings.edit()

        val mac = intent.getStringExtra(EXTRA_MAC_ADDRESS)
        mac?.let {
            editor.setLastConnectedDeviceAddress(it)
            editor.save()
        }

        val intent = Intent(this, Gear360Service::class.java)
        val success = bindService(intent, gearServiceConnection, BIND_AUTO_CREATE)
        Log.d(TAG, "Gear360Service bound $success")

        findViewById<Button>(R.id.btn_connect).setOnClickListener {
            mac?.let {
                Log.d(TAG, "Connect to $it")
                gear360Service?.connect(it)
            }
        }

        findViewById<Button>(R.id.btn_disconnect).setOnClickListener {
            mac?.let {
                Log.d(TAG, "Disconnect from $it")
                gear360Service?.disconnect(it)
            }
        }

        findViewById<Button>(R.id.btn_phone_info).setOnClickListener {
            gear360Service?.messageSender?.sendPhoneInfo()
        }

        findViewById<Button>(R.id.btn_picture).setOnClickListener {
            //gear360Service?.messageSender?.sendTakeImage()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}