package de.teccheck.gear360app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

public class BTConnectorS extends Thread {

    public static final String TAG = BTConstants.TAG_PREFIX + BTConnectorS.class.getSimpleName();

    private BluetoothAdapter adapter;
    private BluetoothDevice device = null;
    private BluetoothServerSocket serverSocket;
    private String mac;

    public BTConnectorS(String mac){
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
            try {
                serverSocket = adapter.listenUsingRfcommWithServiceRecord(BTConstants.TYPE_GEAR_360, BTConstants.GEAR360_UUID);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            BluetoothSocket socket = null;
            while (!isInterrupted()){
                try {
                    socket = this.serverSocket.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(socket.getRemoteDevice().equals(device)){
                    Log.d(TAG, "Connected to " + device.getAddress() + " Name: " + device.getName());
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
