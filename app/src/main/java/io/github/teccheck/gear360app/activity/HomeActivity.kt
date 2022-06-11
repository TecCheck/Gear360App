package io.github.teccheck.gear360app.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import io.github.teccheck.gear360app.LiveTestActivity
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.bluetooth2.Gear360Service
import io.github.teccheck.gear360app.utils.ConnectionState
import io.github.teccheck.gear360app.utils.SettingsHelper
import io.github.teccheck.gear360app.widget.ConnectionDots

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

    private var gear360Service: Gear360Service? = null

    private val gearServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            gear360Service = (service as Gear360Service.LocalBinder).getService()
            gear360Service?.setCallback(gear360ServiceCallback)
            setDeviceConnectivityIndicator(true)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            gear360Service = null
        }
    }

    private val gear360ServiceCallback = object : Gear360Service.Callback {
        override fun onDeviceConnected() {
            setGearConnectivityIndicator(ConnectionState.CONNECTED)
        }

        override fun onDisconnected() {
            setGearConnectivityIndicator(ConnectionState.DISCONNECTED)
        }
    }

    private lateinit var connectionDots: ConnectionDots
    private lateinit var connectionDevice: ImageView
    private lateinit var connectionGear: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        connectionDevice = findViewById(R.id.connect_phone_image)
        connectionDots = findViewById(R.id.dots)
        connectionGear = findViewById(R.id.connect_camera_image)

        setDeviceConnectivityIndicator(false)

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
                setGearConnectivityIndicator(ConnectionState.CONNECTING)
            }
        }

        findViewById<LinearLayout>(R.id.layout_camera).setOnClickListener{
            gear360Service?.messageSender?.sendLiveViewRequest()
        }

        findViewById<Button>(R.id.btn_test).setOnClickListener {
            startActivity(Intent(this, LiveTestActivity::class.java))
        }
    }

    private fun setDeviceConnectivityIndicator(active: Boolean) {
        val colorRes = if (active) {
            R.color.connection_indicators_connected
        } else {
            R.color.connection_indicators_disconnected
        }
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(colorRes)
        } else {
            resources.getColor(colorRes)
        }

        connectionDevice.imageTintList = ColorStateList.valueOf(color)
    }

    private fun setGearConnectivityIndicator(connectionState: ConnectionState) {
        connectionDots.setDotState(connectionState)

        val colorRes = if (connectionState == ConnectionState.CONNECTED) {
            R.color.connection_indicators_connected
        } else {
            R.color.connection_indicators_disconnected
        }
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(colorRes)
        } else {
            resources.getColor(colorRes)
        }

        connectionGear.imageTintList = ColorStateList.valueOf(color)
    }
}