package io.github.teccheck.gear360app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import io.github.teccheck.gear360app.MainActivity;

public class BTService {

    public static final String TAG = BTConstants.TAG_PREFIX + BTService.class.getSimpleName();
    public static String BTName = "";
    public BTSAService btsaService = null;
    private boolean isBound = false;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            btsaService = ((BTSAService.LocalBinder) service).getService();
            //btsaService.startAccessoryManager();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            btsaService = null;
            isBound = false;
        }
    };
    private BluetoothAdapter adapter = null;
    private MainActivity activity = null;

    public void start(MainActivity activity) {
        this.activity = activity;
        isBound = activity.bindService(new Intent(activity, BTSAService.class), mConnection, Context.BIND_AUTO_CREATE);

        if (adapter == null) {
            adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                //EXCEPTION***********************************************************************************************
            }
            BTName = adapter.getName();
        }
    }

    public void connect() {
        activity.setCameraName("Connecting");

        //For testing different Functions
        int type = 3;
        if(type == 1) {
            BTConnectorS connectorS = new BTConnectorS(BTConstants.lastConnectedBTAddress());
            connectorS.start();
        }else if(type == 2){
            BTConnectorC connectorC = new BTConnectorC(BTConstants.lastConnectedBTAddress());
            connectorC.start();
        }else if(type == 3){

            BluetoothDevice device = adapter.getRemoteDevice(BTConstants.lastConnectedBTAddress());
            activity.setCameraName(device.getName());
            activity.setCameraMac(device.getAddress());

            Log.d(TAG, "isBound: " + isBound + ", btsaService: " + btsaService);
            if (isBound && btsaService != null) {
                Log.d(TAG, "findPeers()");
                btsaService.findPeers();
            }
        }else if(type == 4){
            try {
                //btsaService.startAccessoryManager();
                btsaService.samAccessoryManager.connect(BTConstants.lastConnectedBTAddress(), 2, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onConnect(){
        BTMessageSender.sendPhoneInfo(this);
        BTMessageSender.sendWidgetInfoRequest(this);
        BTMessageSender.sendDateTimeRequest(this);
    }

    public void connectOld() {
        Log.d(TAG, "connect()");
        if (!adapter.isEnabled()) {
            adapter.enable();
        }

        if (BTConstants.hasLastConnected) {
            BluetoothDevice device = adapter.getRemoteDevice(BTConstants.lastConnectedBTAddress());
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                device.createBond();
            }

            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                activity.setCameraName("Connecting");
                //AutoConnector connector = new AutoConnector(activity.getApplicationContext(), this);
                //connector.setName("Connector");
                //connector.start();
                try {
                    btsaService.samAccessoryManager.connect(BTConstants.lastConnectedBTAddress(), 2, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            final BroadcastReceiver receiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device.getUuids() != null && device.getUuids()[0].getUuid().equals(BTConstants.GEAR360_UUID)) {
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address

                            Log.d(TAG, "Device found: " + deviceName + " : " + deviceHardwareAddress);
                            adapter.cancelDiscovery();
                            device.createBond();
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(receiver, filter);
            adapter.startDiscovery();
        }
    }

    public void onConnectedOld(BluetoothSocket socket) {
        Log.d(TAG, "Connected");
        BluetoothDevice device = socket.getRemoteDevice();
        activity.setCameraName(device.getName());
        activity.setCameraMac(device.getAddress());

        Log.d(TAG, "isBound: " + isBound + ", btsaService: " + btsaService);
        if (isBound && btsaService != null) {
            Log.d(TAG, "findPeers()");
            btsaService.findPeers();
            BTMessageSender.sendPhoneInfo(this);
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        btsaService.closeConnection();
    }

    public void send(int channel, String data) {
        btsaService.sendData(channel, data.getBytes());
    }
}