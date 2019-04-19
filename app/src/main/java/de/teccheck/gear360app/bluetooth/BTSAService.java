package de.teccheck.gear360app.bluetooth;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;
import com.samsung.android.sdk.accessorymanager.SamAccessoryManager;
import com.samsung.android.sdk.accessorymanager.SamDevice;

import java.io.IOException;

import de.teccheck.gear360app.R;

public class BTSAService extends SAAgent {

    public static final String TAG = "G360_" + BTSAService.class.getSimpleName();

    private static final Class<ServiceConnection> SASOCKET_CLASS = ServiceConnection.class;
    private final IBinder mBinder = new LocalBinder();
    Handler mHandler = new Handler();
    private ServiceConnection mConnectionHandler = null;
    public SamAccessoryManager samAccessoryManager = null;
    public SamAccessoryManager.AccessoryEventListener accessoryEventListener = null;

    public BTSAService() {
        super(TAG, SASOCKET_CLASS);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SA mAccessory = new SA();
        try {
            mAccessory.initialize(this);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onFindPeerAgentsResponse(SAPeerAgent[] peerAgents, int result) {
        if ((result == SAAgent.PEER_AGENT_FOUND) && (peerAgents != null)) {
            for (SAPeerAgent peerAgent : peerAgents)
                requestServiceConnection(peerAgent);
        } else if (result == SAAgent.FINDPEER_DEVICE_NOT_CONNECTED) {
            Toast.makeText(getApplicationContext(), "FINDPEER_DEVICE_NOT_CONNECTED", Toast.LENGTH_LONG).show();
        } else if (result == SAAgent.FINDPEER_SERVICE_NOT_FOUND) {
            Toast.makeText(getApplicationContext(), "FINDPEER_SERVICE_NOT_FOUND", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoPeersFound, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        if (peerAgent != null) {
            acceptServiceConnectionRequest(peerAgent);
        }
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket socket, int result) {
        if (result == SAAgent.CONNECTION_SUCCESS) {
            this.mConnectionHandler = (ServiceConnection) socket;
            Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_LONG).show();
        } else if (result == SAAgent.CONNECTION_ALREADY_EXIST) {
            Toast.makeText(getBaseContext(), "Connection already exists", Toast.LENGTH_LONG).show();
        } else if (result == SAAgent.CONNECTION_DUPLICATE_REQUEST) {
            Toast.makeText(getBaseContext(), "Connection duplicate request", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.ConnectionFailure, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onError(SAPeerAgent peerAgent, String errorMessage, int errorCode) {
        super.onError(peerAgent, errorMessage, errorCode);
    }

    @Override
    protected void onPeerAgentsUpdated(SAPeerAgent[] peerAgents, int result) {
        final SAPeerAgent[] peers = peerAgents;
        final int status = result;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (peers != null) {
                    if (status == SAAgent.PEER_AGENT_AVAILABLE) {
                        Toast.makeText(getApplicationContext(), "PEER_AGENT_AVAILABLE", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "PEER_AGENT_UNAVAILABLE", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void findPeers() {
        findPeerAgents();
    }

    public void sendData(int channel, byte[] data) {
        if (mConnectionHandler != null) {
            try {
                mConnectionHandler.send(channel, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean closeConnection() {
        if (mConnectionHandler != null) {
            mConnectionHandler.close();
            mConnectionHandler = null;
            return true;
        } else {
            return false;
        }
    }

    public void startAccessoryManager() {
        accessoryEventListener = new SAEventListener();
        try {
            samAccessoryManager = SamAccessoryManager.getInstance(getApplicationContext(), accessoryEventListener);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }
    }

    public class ServiceConnection extends SASocket {
        public ServiceConnection() {
            super(ServiceConnection.class.getName());
        }

        @Override
        public void onError(int channelId, String errorMessage, int errorCode) {
        }

        @Override
        public void onReceive(int channelId, byte[] data) {
            BTResponder.conformAndResponseCommandJson(data);
        }

        @Override
        protected void onServiceConnectionLost(int reason) {
            closeConnection();
        }
    }

    public class LocalBinder extends Binder {
        public BTSAService getService() {
            return BTSAService.this;
        }
    }

    public class SAEventListener implements SamAccessoryManager.AccessoryEventListener {
        public void onAccessoryConnected(SamDevice acc) {

        }

        public void onAccessoryDisconnected(SamDevice acc, int reason) {

        }

        public void onError(SamDevice acc, int reason) {

        }

        public void onAccountLoggedIn(SamDevice arg0) {
        }

        public void onAccountLoggedOut(SamDevice arg0) {
        }
    }
}