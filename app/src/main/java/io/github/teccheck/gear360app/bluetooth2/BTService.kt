package io.github.teccheck.gear360app.bluetooth2

import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.samsung.android.sdk.accessory.SAAgentV2
import com.samsung.android.sdk.accessory.SAAgentV2.RequestAgentCallback
import com.samsung.android.sdk.accessorymanager.SamDevice
import io.github.teccheck.gear360app.bluetooth.BTMessages

private const val TAG = "BTService"

class BTService : Service() {
    private val binder: IBinder = LocalBinder()

    private var btmsaService: BTMSAService? = null
    private var btmProviderService: BTMProviderService? = null

    private val btmsaServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            btmsaService = (service as BTMSAService.LocalBinder).getService()
            btmsaService?.setStatusCallback(btmsaStatusCallback)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: $name")
            btmsaService = null
            btmProviderService = null
        }
    }

    private val btmsaStatusCallback = object : BTMSAService.StatusCallback {
        override fun onAccessoryConnected(device: SamDevice) {
            Log.d(TAG, "onAccessoryConnected $device")
            SAAgentV2.requestAgent(
                applicationContext,
                BTMProviderService::class.java.name,
                agentCallback
            )
        }

        override fun onAccessoryDisconnected(device: SamDevice, reason: Int) {
            Log.d(TAG, "onAccessoryDisconnected $device, $reason")
        }

        override fun onError(device: SamDevice, reason: Int) {
            Log.d(TAG, "onError$device, $reason")
        }
    }

    private val btmStatusCallback = object : BTMProviderService.StatusCallback {
        override fun onConnectDevice(name: String?, peer: String?, product: String?) {
            Log.d(TAG, "onConnectDevice $name, $peer, $product")
            sendPhoneInfo()
        }

        override fun onError(result: Int) {
            Log.d(TAG, "onError $result")
        }

        override fun onReceive(channelId: Int, data: ByteArray?) {
            Log.d(TAG, "onReceive $channelId, $data")
        }

        override fun onServiceDisconnection() {
            Log.d(TAG, "onServiceDisconnection")
            btmProviderService = null
        }

    }

    private val agentCallback = object : RequestAgentCallback {
        override fun onAgentAvailable(agent: SAAgentV2) {
            Log.d(TAG, "Agent available: $agent")
            btmProviderService = agent as BTMProviderService
            btmProviderService?.setup(btmStatusCallback)
        }

        override fun onError(errorCode: Int, message: String) {
            Log.d(TAG, "onError $errorCode, $message")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        val success = bindService(
            Intent(this, BTMSAService::class.java),
            btmsaServiceConnection,
            BIND_AUTO_CREATE
        )

        Log.d(TAG, "BTMSAService bound $success")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        return

        btmProviderService?.let {
            it.closeConnection()
            it.releaseAgent()
            btmProviderService = null
        }
        unbindService(btmsaServiceConnection)

    }

    fun connect(address: String) {
        Log.d(TAG, "connect $address")
        // TODO: Check paired device?
        btmsaService?.connect(address)
    }

    fun disconnect(address: String) {
        Log.d(TAG, "disconnect $address")
        return

        btmsaService?.disconnect(address)
        btmProviderService?.releaseAgent()
    }

    fun sendPhoneInfo() {
        Log.d(TAG, "sendPhoneInfo")
        val versionName = "1.2.00.8"
        val message = BTMessages.BTInfoMsg(
            BTMessages.IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE,
            "100",
            BTMessages.IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE,
            "100",
            versionName,
            false
        )
        sendMessage(message)
    }

    fun sendTakeImage() {
        Log.d(TAG, "sendTakeImage")
        val message = BTMessages.BTShotMsg(BTMessages.IDS.REMOTE_SHOT_REQUEST_MSGID, "capture")
        sendMessage(message)
    }

    private fun sendMessage(btMessage: BTMessages.BTMessage) {
        Log.d(TAG, "sendMessage $btmProviderService")
        btmProviderService?.sendData(204, btMessage.toJSON().toString().encodeToByteArray())
    }

    override fun onBind(intent: Intent?): IBinder {
        return this.binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): BTService {
            return this@BTService
        }
    }
}