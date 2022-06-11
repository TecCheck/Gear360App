package io.github.teccheck.gear360app.bluetooth2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import com.samsung.android.sdk.accessory.SAAgentV2
import com.samsung.android.sdk.accessorymanager.SamAccessoryManager
import com.samsung.android.sdk.accessorymanager.SamDevice
import io.github.teccheck.gear360app.bluetooth.BTMessages

private const val TAG = "Gear360Service"
private const val SA_TRANSPORT_TYPE = SamAccessoryManager.TRANSPORT_BT

class Gear360Service : Service(), MessageSender.Sender {

    private val binder = LocalBinder()

    private var connectedDeviceAddress: String? = null
    private var callback: Callback? = null;

    private var samAccessoryManager: SamAccessoryManager? = null
    private val samListener = object : SamAccessoryManager.AccessoryEventListener {
        override fun onAccessoryConnected(device: SamDevice) {
            Log.d(TAG, "onAccessoryConnected $device")
            setupBTMProviderService()
        }

        override fun onAccessoryDisconnected(device: SamDevice, reason: Int) {
            Log.d(TAG, "onAccessoryDisconnected $device, $reason")
        }

        override fun onError(device: SamDevice?, reason: Int) {
            Log.d(TAG, "samListener onError $device, $reason")
        }

        override fun onAccountLoggedIn(device: SamDevice) {}
        override fun onAccountLoggedOut(device: SamDevice) {}
    }

    private var btmProviderService: BTMProviderService? = null
    private val btmStatusCallback = object : BTMProviderService.StatusCallback {
        override fun onConnectDevice(name: String?, peer: String?, product: String?) {
            Log.d(TAG, "onConnectDevice $name, $peer, $product")
            callback?.onDeviceConnected()
        }

        override fun onError(result: Int) {
            Log.d(TAG, "btmStatusCallback onError $result")
        }

        override fun onReceive(channelId: Int, data: ByteArray?) {
            Log.d(TAG, "onReceive $channelId, $data")
        }

        override fun onServiceDisconnection() {
            Log.d(TAG, "onServiceDisconnection")
            btmProviderService?.closeConnection()
            callback?.onDisconnected()
        }
    }

    val messageSender = MessageSender(this)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        // SamAccessoryManager needs to be initialised on another thread for whatever reason
        val handlerThread = HandlerThread("$TAG SAThread")
        handlerThread.start()

        if (handlerThread.looper == null)
            return

        val handler = Handler(handlerThread.looper)
        handler.post {
            samAccessoryManager = SamAccessoryManager.getInstance(applicationContext, samListener)
            handlerThread.quitSafely()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

        disconnect()
        samAccessoryManager?.release()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun connect(address: String) {
        samAccessoryManager?.let {
            it.connect(address, SA_TRANSPORT_TYPE)
            connectedDeviceAddress = address
        }
    }

    fun disconnect(address: String? = connectedDeviceAddress) {
        btmProviderService?.closeConnection()
        btmProviderService?.releaseAgent()
        address?.let { samAccessoryManager?.disconnect(it, SA_TRANSPORT_TYPE) }
    }

    private fun setupBTMProviderService() {
        Log.d(TAG, "setupProviderService")
        val requestAgentCallback = object : SAAgentV2.RequestAgentCallback {
            override fun onAgentAvailable(agent: SAAgentV2) {
                Log.d(TAG, "Agent available: $agent")
                btmProviderService = agent as BTMProviderService
                btmProviderService?.setup(btmStatusCallback)
                btmProviderService?.findSaPeers()
            }

            override fun onError(errorCode: Int, message: String) {
                Log.d(TAG, "requestAgentCallback onError $errorCode, $message")
            }
        }

        SAAgentV2.requestAgent(
            applicationContext,
            BTMProviderService::class.java.name,
            requestAgentCallback
        )
    }

    override fun send(channelId: Int, data: ByteArray) {
        btmProviderService?.sendData(204, data)
    }

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

    interface Callback {
        fun onDeviceConnected()

        fun onDisconnected()
    }
}