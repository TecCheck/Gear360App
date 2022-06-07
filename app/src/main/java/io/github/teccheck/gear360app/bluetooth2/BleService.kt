package io.github.teccheck.gear360app.bluetooth2

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import io.github.teccheck.gear360app.bluetooth.BTConstants
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

private const val TAG = "BleService"

open class BleService : Service() {
    private val binder = LocalBinder()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null

    private var connectionState = STATE_DISCONNECTED

    private val bluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                connectionState = STATE_CONNECTED
                broadcastUpdate(ACTION_GATT_CONNECTED)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                connectionState = STATE_DISCONNECTED
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
        }
    }

    fun init(): Boolean {
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Log.e(TAG, "No Bluetooth adapter found.")
            return false
        }

        bluetoothAdapter?.let {
            if (!it.isEnabled)
                it.enable()
        }

        return true
    }

    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
                return true
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.  Unable to connect.")
                return false
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return false
        }
    }

    fun discoverServices() {
        val bluetoothGatt = this.bluetoothGatt ?: return

        Log.d(TAG, "Discover Services")
        bluetoothGatt.discoverServices()
    }

    fun readServices() {
        val bluetoothGatt = this.bluetoothGatt ?: return

        Log.d(TAG, "Available Services")
        for (service in bluetoothGatt.services) {
            Log.d(TAG, "* Service ${service.uuid}")

            for (characteristic in service.characteristics) {
                Log.d(TAG, "  * Characteristic ${characteristic.uuid}")
                bluetoothGatt.readCharacteristic(characteristic)

                for (descriptor in characteristic.descriptors) {
                    Log.d(TAG, "    * Descriptor ${descriptor.uuid}")
                    bluetoothGatt.readDescriptor(descriptor)
                }
            }
        }
    }

    fun showServiceValues() {
        val bluetoothGatt = this.bluetoothGatt ?: return

        Log.d(TAG, "Available Services")
        for (service in bluetoothGatt.services) {
            Log.d(TAG, "* Service ${service.uuid}")

            for (characteristic in service.characteristics) {
                val value = if (characteristic.value == null)
                    "null"
                else
                    String(characteristic.value)

                Log.d(TAG, "  * Characteristic ${characteristic.uuid}: $value")

                for (descriptor in characteristic.descriptors) {
                    val value = if (descriptor.value == null)
                        "null"
                    else
                        String(characteristic.value)

                    Log.d(TAG, "    * Descriptor ${descriptor.uuid}: $value")
                }
            }
        }
    }


    private fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        fun getService(): BleService {
            return this@BleService
        }
    }

    companion object {
        const val ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2
    }
}