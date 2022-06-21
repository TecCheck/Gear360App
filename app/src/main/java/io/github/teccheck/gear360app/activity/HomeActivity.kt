package io.github.teccheck.gear360app.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.bluetooth2.Gear360Service
import io.github.teccheck.gear360app.utils.ConnectionState
import io.github.teccheck.gear360app.utils.SettingsHelper
import io.github.teccheck.gear360app.widget.ConnectionDots

private const val TAG = "HomeActivity"
const val EXTRA_MAC_ADDRESS = "mac_address"

class HomeActivity : AppCompatActivity() {

    private val mainHandler = Handler(Looper.getMainLooper())
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
        override fun onSAMStarted() {
            connect()
        }

        override fun onDeviceConnected() {
            mainHandler.post { setGearConnectivityIndicator(ConnectionState.CONNECTED) }
        }

        override fun onDeviceDisconnected() {
            mainHandler.post { setGearConnectivityIndicator(ConnectionState.DISCONNECTED) }
        }
    }

    private lateinit var connectionDots: ConnectionDots
    private lateinit var connectionDevice: ImageView
    private lateinit var connectionGear: ImageView
    private lateinit var connectButton: Button

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

        connectButton = findViewById(R.id.btn_connect)
        connectButton.setOnClickListener {
            connect()
        }

        findViewById<LinearLayout>(R.id.layout_camera).setOnClickListener {
            gear360Service?.messageSender?.sendLiveViewRequest()
        }

        findViewById<LinearLayout>(R.id.layout_remote_control).setOnClickListener {
            startActivity(Intent(this, RemoteControlActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layout_hardware).setOnClickListener {
            startActivity(Intent(this, HardwareInfoActivity::class.java))
        }

        findViewById<Button>(R.id.btn_test).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    private fun connect() {
        intent.getStringExtra(EXTRA_MAC_ADDRESS)?.let {
            Log.d(TAG, "Connect to $it")
            gear360Service?.connect(it)
            setGearConnectivityIndicator(ConnectionState.CONNECTING)
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

        when (connectionState) {
            ConnectionState.DISCONNECTED -> {
                connectButton.visibility = View.VISIBLE
                connectButton.isEnabled = true
            }
            ConnectionState.CONNECTING -> {
                connectButton.visibility = View.VISIBLE
                connectButton.isEnabled = false
            }
            ConnectionState.CONNECTED -> {
                connectButton.visibility = View.GONE
                connectButton.isEnabled = false
            }
        }
    }
}