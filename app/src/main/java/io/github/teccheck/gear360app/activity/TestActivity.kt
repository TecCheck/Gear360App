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
import io.github.teccheck.gear360app.bluetooth2.BTService
import io.github.teccheck.gear360app.utils.SettingsHelper

private const val TAG = "ControlActivity"
const val EXTRA_MAC_ADDRESS = "mac_address"

class TestActivity : AppCompatActivity() {

    private var btService: BTService? = null

    private val btServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            btService = (service as BTService.LocalBinder).getService()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            btService = null
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

        startBTService()

        findViewById<Button>(R.id.btn_connect).setOnClickListener {
            mac?.let {
                Log.d(TAG, "Connect to $it")
                btService?.connect(it)
            }
        }

        findViewById<Button>(R.id.btn_disconnect).setOnClickListener {
            mac?.let {
                Log.d(TAG, "Disconnect from $it")
                btService?.disconnect(it)
            }
        }

        findViewById<Button>(R.id.btn_phone_info).setOnClickListener {
            btService?.sendPhoneInfo()
        }

        findViewById<Button>(R.id.btn_picture).setOnClickListener {
            btService?.sendTakeImage()
        }
    }

    override fun onDestroy() {
        intent.getStringExtra(EXTRA_MAC_ADDRESS)?.let { btService?.disconnect(it) }
        super.onDestroy()
    }

    private fun startBTService() {
        /*
        val accessory = SA()
        try {
            accessory.initialize(this)
            accessory.isFeatureEnabled(SA.DEVICE_ACCESSORY)
        } catch (e: Exception) {
            e.printStackTrace()
        }
         */

        val intent = Intent(this, BTService::class.java)
        val success = bindService(intent, btServiceConnection, BIND_AUTO_CREATE)
        Log.d(TAG, "BTService bound $success")
    }
}