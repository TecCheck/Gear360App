package io.github.teccheck.gear360app.bluetooth2

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.samsung.android.sdk.accessory.SAAgentV2
import com.samsung.android.sdk.accessorymanager.SamAccessoryManager
import com.samsung.android.sdk.accessorymanager.SamDevice

private const val TAG = "Gear360Service"
private const val SA_TRANSPORT_TYPE = SamAccessoryManager.TRANSPORT_BT

class Gear360Service : Service() {

    private val binder = LocalBinder()

    private val handler = Handler(Looper.getMainLooper())

    private var connectedDeviceAddress: String? = null
    private var callback: Callback? = null

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
            if (reason == SamAccessoryManager.ERROR_ACCESSORY_ALREADY_CONNECTED) {
                Log.d(TAG, "Already connected")
                setupBTMProviderService()
            } else {
                callback?.onDeviceDisconnected()
            }
        }

        override fun onAccountLoggedIn(device: SamDevice) {}
        override fun onAccountLoggedOut(device: SamDevice) {}
    }

    private var btmProviderService: BTMProviderService? = null
    private val btmStatusCallback = object : BTMProviderService.StatusCallback {
        override fun onConnectDevice(name: String?, peer: String?, product: String?) {
            Log.d(TAG, "onConnectDevice $name, $peer, $product")
            handler.postDelayed({ messageSender.sendPhoneInfo() }, 2000)
            callback?.onDeviceConnected()
        }

        override fun onError(result: Int) {
            Log.d(TAG, "btmStatusCallback onError $result")
        }

        override fun onReceive(channelId: Int, data: ByteArray?) {
            Log.d(TAG, "onReceive $channelId, $data")
            messageHandler.onReceive(channelId, data)
        }

        override fun onServiceDisconnection() {
            Log.d(TAG, "onServiceDisconnection")
            btmProviderService?.closeConnection()
            callback?.onDeviceDisconnected()
        }
    }

    private val messageListener = object : MessageHandler.MessageListener {
        override fun onMessageReceive(message: BTMessage) {
            when (message) {
                is BTConfigMsg -> {
                    gear360Configs.setConfigs(message.configs)
                }
                is BTInfoRsp -> {
                    gear360Info = Gear360Info(
                        message.modelName,
                        message.modelVersion,
                        message.channel,
                        message.wifiDirectMac,
                        message.apSSID,
                        message.apPassword,
                        message.boardRevision,
                        message.serialNumber,
                        message.uniqueNumber,
                        message.wifiMac,
                        message.btMac,
                        message.btFotaTestUrl,
                        message.fwType
                    )

                    Log.d(
                        TAG,
                        "Version: ${gear360Info?.getSemanticVersion()} -- ${gear360Info?.getVersionName()}"
                    )
                }
                is BTWidgetReq -> {
                    messageSender.sendWidgetInfoRequest()
                }
                is BTWidgetRsp -> {
                    gear360Status = message.gear360Status
                }
            }
        }
    }

    val messageHandler = MessageHandler()
    val messageSender = MessageSender(object : MessageSender.Sender {
        override fun send(channelId: Int, data: ByteArray) {
            btmProviderService?.send(channelId, data)
        }
    })

    val gear360Configs = Gear360Configs()
    var gear360Info: Gear360Info? = null
    var gear360Status: Gear360Status? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()

        messageHandler.addMessageListener(messageListener)

        // SamAccessoryManager needs to be initialised on another thread for whatever reason
        val handlerThread = HandlerThread("$TAG SAThread")
        handlerThread.start()

        if (handlerThread.looper == null)
            return

        val handler = Handler(handlerThread.looper)
        handler.post {
            samAccessoryManager = SamAccessoryManager.getInstance(applicationContext, samListener)
            Handler(Looper.getMainLooper()).post { callback?.onSAMStarted() }
            handlerThread.quitSafely()
        }
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind")
        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        messageHandler.removeMessageListener(messageListener)

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
        fun onSAMStarted()

        fun onDeviceConnected()

        fun onDeviceDisconnected()
    }
}