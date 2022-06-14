package io.github.teccheck.gear360app.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.bluetooth2.Gear360Service

private const val TAG = "HardwareInfoActivity"

class HardwareInfoActivity : AppCompatActivity() {

    private var gear360Service: Gear360Service? = null

    private val gearServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            gear360Service = (service as Gear360Service.LocalBinder).getService()
            startRecyclerView()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            gear360Service = null
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardware_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler_view)

        val intent = Intent(this, Gear360Service::class.java)
        val success = bindService(intent, gearServiceConnection, BIND_AUTO_CREATE)
        Log.d(TAG, "Gear360Service bound $success")
    }

    fun startRecyclerView() {
        val info = gear360Service?.gear360Info ?: return

        Log.d(TAG, "startRecyclerView")

        val modelNameIcon =
            if (info.isCM200()) R.drawable.ic_gear_360_2016
            else R.drawable.ic_gear_360_2017

        val fwTypeName =
            if (info.fwType == 0) getString(R.string.hardware_fw_type_user)
            else getString(R.string.hardware_fw_type_retail)

        val dataSet: Array<Property> = arrayOf(
            Property(
                modelNameIcon,
                R.string.hardware_model_name,
                info.modelName
            ),
            Property(
                R.drawable.ic_baseline_memory_24,
                R.string.hardware_board_revision,
                info.boardRevision
            ),
            Property(
                R.drawable.ic_baseline_build_24,
                R.string.hardware_serial_number,
                info.serialNumber
            ),
            Property(
                R.drawable.ic_baseline_numbers_24,
                R.string.hardware_unique_number,
                info.uniqueNumber
            ),
            Property(
                R.drawable.ic_baseline_widgets_24,
                R.string.hardware_model_version,
                "${info.getVersionName()} (${info.getSemanticVersion()})"
            ),
            Property(
                R.drawable.ic_baseline_settings_applications_24,
                R.string.hardware_fw_type,
                "${info.fwType} ($fwTypeName)"
            ),
            Property(
                R.drawable.ic_baseline_looks_24,
                R.string.hardware_channel,
                info.channel.toString()
            ),
            Property(
                R.drawable.ic_baseline_font_download_24,
                R.string.hardware_wifi_direct_mac,
                info.wifiDirectMac
            ),
            Property(
                R.drawable.ic_baseline_network_wifi_24,
                R.string.hardware_ap_ssid,
                info.apSSID
            ),
            Property(
                R.drawable.ic_baseline_password_24,
                R.string.hardware_ap_password,
                info.apPassword
            ),
            Property(
                R.drawable.ic_baseline_font_download_24,
                R.string.hardware_wifi_mac,
                info.wifiMac
            ),
            Property(
                R.drawable.ic_baseline_bluetooth_24,
                R.string.hardware_bt_mac,
                info.btMac
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(dataSet)
    }

    data class Property(val iconResource: Int, val nameResource: Int, val value: String)

    class RecyclerAdapter(private val dataSet: Array<Property>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_entry_hardware, viewGroup, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val view = viewHolder.itemView
            val property = dataSet[position]

            view.findViewById<ImageView>(R.id.icon).setImageResource(property.iconResource)
            view.findViewById<TextView>(R.id.name).setText(property.nameResource)
            view.findViewById<TextView>(R.id.value).text = property.value
        }

        override fun getItemCount() = dataSet.size
    }
}