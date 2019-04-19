package de.teccheck.gear360app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class AutoConnector extends Thread {

    public static final String TAG = BTConstants.TAG_PREFIX + AutoConnector.class.getSimpleName();

    private String NO_MSG = "No";
    private BluetoothAdapter bluetoothAdapter;
    private Context context = null;
    private BluetoothServerSocket serverSocket;
    private BTService btService;

    public AutoConnector(Context context, BTService service){
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btService = service;
        try {
            serverSocket = this.bluetoothAdapter.listenUsingRfcommWithServiceRecord(BTConstants.TYPE_GEAR_360, BTConstants.GEAR360_UUID);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void run(){
        BluetoothSocket socket = null;
        while (!isInterrupted()) {
            Log.d(TAG, "while");
            try {
                socket = this.serverSocket.accept();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (socket != null) {
                if (!BTConstants.lastConnectedBTAddress().equals(socket.getRemoteDevice().getAddress())) {
                    Log.d(TAG, "Wrong Device Connected");
                    socket = null; //do not?
                }else {
                    Log.d(TAG, "Connected");
                    //BTService.getInstance().BTMConnectionStart(socket.getRemoteDevice());

                    final BluetoothSocket finalSocket = socket;
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = () -> {
                        btService.onConnectedOld(finalSocket);
                    };
                    mainHandler.post(myRunnable);
                    interrupt();
                    this.stop();
                }
            }
        }
    }
}
