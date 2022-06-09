package io.github.teccheck.gear360app.bluetooth2

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.samsung.android.sdk.accessorymanager.SamAccessoryManager
import com.samsung.android.sdk.accessorymanager.SamAccessoryManager.AccessoryEventListener
import com.samsung.android.sdk.accessorymanager.SamDevice

private const val TAG = "BTMSAService"

private const val MSG_START_SAM = 1
private const val MSG_CONNECT_DEVICE = 2
private const val MSG_DISCONNECT_DEVICE = 3

private const val MSG_DATA_DEVICE_ADDRESS = "address"

private const val SA_TRANSPORT_TYPE = SamAccessoryManager.TRANSPORT_BT

class BTMSAService : Service() {
    private val binder: IBinder = LocalBinder()

    private var handlerThread: HandlerThread? = null
    private var handler: SAHandler? = null

    private var samAccessoryManager: SamAccessoryManager? = null
    private var statusCallback: StatusCallback? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        handlerThread = HandlerThread("$TAG SAThread")
        handlerThread?.let {
            it.start()
            if (it.looper != null)
                handler = SAHandler(it.looper)
        }
        handler?.sendEmptyMessage(MSG_START_SAM)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        handlerThread?.let {
            it.quit()
            it.interrupt()
            handlerThread = null
        }

        samAccessoryManager?.release()
        super.onDestroy()
    }

    fun setStatusCallback(statusCallback: StatusCallback) {
        Log.d(TAG, "setStatusCallback")
        this.statusCallback = statusCallback
    }

    fun connect(address: String) {
        Log.d(TAG, "connect $address")
        samAccessoryManager?.connect(address, SA_TRANSPORT_TYPE)
        return

        val message = Message()
        message.what = MSG_CONNECT_DEVICE
        message.data = Bundle()
        message.data.putString(MSG_DATA_DEVICE_ADDRESS, address)
        handler?.sendMessage(message)
    }

    fun disconnect(address: String) {
        Log.d(TAG, "disconnect $address")
        samAccessoryManager?.disconnect(address, SA_TRANSPORT_TYPE)
        return

        val message = Message()
        message.what = MSG_DISCONNECT_DEVICE
        message.data = Bundle()
        message.data.putString(MSG_DATA_DEVICE_ADDRESS, address)
        handler?.sendMessage(message)
    }

    override fun onBind(intent: Intent?): IBinder {
        return this.binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): BTMSAService {
            return this@BTMSAService
        }
    }

    inner class SAEventListener : AccessoryEventListener {
        override fun onAccessoryConnected(device: SamDevice) {
            Log.d(TAG, "onAccessoryConnected $device")
            statusCallback?.onAccessoryConnected(device)
        }

        override fun onAccessoryDisconnected(device: SamDevice, reason: Int) {
            Log.d(TAG, "onAccessoryDisconnected $device, $reason")
            statusCallback?.onAccessoryDisconnected(device, reason)
        }

        override fun onError(device: SamDevice?, reason: Int) {
            Log.d(TAG, "onError $device, $reason")
            //statusCallback?.onError(acc, reason)
        }

        override fun onAccountLoggedIn(device: SamDevice) {}

        override fun onAccountLoggedOut(device: SamDevice) {}
    }

    inner class SAHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_START_SAM -> {
                    samAccessoryManager =
                        SamAccessoryManager.getInstance(this@BTMSAService, SAEventListener())
                    return
                }
                MSG_CONNECT_DEVICE -> {
                    Log.d(TAG, "MSG_CONNECT_DEVICE: ${msg.data.getString(MSG_DATA_DEVICE_ADDRESS)}")
                    samAccessoryManager?.connect(msg.data.getString(MSG_DATA_DEVICE_ADDRESS), 2, 0)
                    return
                }
                MSG_DISCONNECT_DEVICE -> {
                    samAccessoryManager?.disconnect(msg.data.getString(MSG_DATA_DEVICE_ADDRESS), 2)
                    return
                }
                else -> return
            }
        }
    }

    interface StatusCallback {
        fun onAccessoryConnected(device: SamDevice)

        fun onAccessoryDisconnected(device: SamDevice, reason: Int)

        fun onError(device: SamDevice, reason: Int)
    }
}