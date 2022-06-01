package io.github.teccheck.gear360app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

public class BTConnectorC extends Thread {

    public static final String TAG = BTConstants.TAG_PREFIX + BTConnectorC.class.getSimpleName();

    private BluetoothAdapter adapter;
    private BluetoothDevice device = null;
    private String mac;

    public BTConnectorC(String mac){
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.mac = mac;
        setName(TAG);
    }

    public void run(){
        if(!adapter.isEnabled()){
            adapter.enable();
            while (!adapter.isEnabled()){
                sleepMillis(10);
            }
        }

        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : bondedDevices){
            if(bluetoothDevice.getAddress().equalsIgnoreCase(mac)){
                device = bluetoothDevice;
                break;
            }
        }

        if(device == null){
            device = adapter.getRemoteDevice(mac);
            if(device != null){
                device.createBond();
            }
        }

        if(device != null){
            adapter.cancelDiscovery();
            BluetoothSocket socket = null;
            try {
                socket = device.createRfcommSocketToServiceRecord(BTConstants.GEAR360_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }else {
            Log.d(TAG, "Device not Reached");
            //EXCEPTION******************
        }
    }

    public void sleepMillis(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
