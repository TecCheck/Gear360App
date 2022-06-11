package io.github.teccheck.gear360app.activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import io.github.teccheck.gear360app.R

private const val TAG = "ScanActivity"
private const val SCAN_PERIOD: Long = 10000


class ScanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "Found ${result.device}")
            (recyclerView.adapter as BtDeviceAdapter).addDevice(result.device)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BtDeviceAdapter()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bluetoothManager = getSystemService(BluetoothManager::class.java)
            this.bluetoothAdapter = bluetoothManager.adapter
        } else {
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }
        this.bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        startRefresh()
    }

    override fun onDestroy() {
        scanning = false
        bluetoothLeScanner.stopScan(leScanCallback)
        super.onDestroy()
    }

    private fun startRefresh() {
        scan()
    }

    private fun scan() {
        if (scanning)
            return

        handler.postDelayed({
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }, SCAN_PERIOD)

        (recyclerView.adapter as BtDeviceAdapter).clear()
        scanning = true
        bluetoothLeScanner.startScan(leScanCallback)
    }

    private fun onItemClick(index: Int, bluetoothDevice: BluetoothDevice) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(EXTRA_MAC_ADDRESS, bluetoothDevice.address)
        startActivity(intent)
    }

    inner class BtDeviceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val devices = mutableListOf<BluetoothDevice>()

        fun addDevice(device: BluetoothDevice) {
            if (devices.contains(device))
                return

            devices.add(device)
            notifyItemInserted(devices.size - 1)
        }

        fun clear() {
            val lastIndex = devices.lastIndex
            notifyItemRangeRemoved(0, lastIndex)
            devices.clear()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = layoutInflater.inflate(R.layout.list_entry_device, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder.itemView
            val device = devices[position]

            val chip = view.findViewById<Chip>(R.id.chip)
            chip.setOnClickListener { this@ScanActivity.onItemClick(position, device) }
            chip.text = device.name
        }

        override fun getItemCount(): Int {
            return devices.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }
}