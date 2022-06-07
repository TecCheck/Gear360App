package io.github.teccheck.gear360app.bluetooth2;

import android.content.Context;
import android.util.Log;

import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAAgentV2;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;
import java.util.Arrays;

public class BTMProviderService extends SAAgentV2 {

    private static final String TAG = "BTMProviderService";

    private BTMProviderConnection providerConnection = null;
    private StatusCallback callback = null;

    public BTMProviderService(Context context) {
        super(TAG, context, BTMProviderConnection.class);

        SA mAccessory = new SA();
        try {
            mAccessory.initialize(getApplicationContext());
            mAccessory.isFeatureEnabled(0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setup(StatusCallback callback) {
        this.callback = callback;
        findPeerAgents();
    }

    public void sendData(int channel, byte[] data) {
        Log.d(TAG, "SendData: " + new String(data));
        if (providerConnection != null) {
            try {
                providerConnection.send(channel, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(TAG, "providerConnection is null");
        }
    }

    public boolean closeConnection() {
        if (providerConnection == null)
            return false;

        providerConnection.close();
        providerConnection = null;
        return true;
    }

    @Override
    protected void onFindPeerAgentsResponse(SAPeerAgent[] peerAgents, int result) {
        Log.d(TAG, "onFindPeerAgentsResponse " + Arrays.toString(peerAgents) + " " + result);
        if ((result == SAAgent.PEER_AGENT_FOUND) && (peerAgents != null))
            for (SAPeerAgent peerAgent : peerAgents)
                requestServiceConnection(peerAgent);

    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        Log.d(TAG, "onServiceConnectionRequested " + peerAgent);
        //if (peerAgent != null)
        acceptServiceConnectionRequest(peerAgent);
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket socket, int result) {
        Log.d(TAG, "onServiceConnectionResponse " + peerAgent + " " + socket + " " + result);
        if (result == SAAgent.CONNECTION_SUCCESS) {
            providerConnection = (BTMProviderConnection) socket;
            if (callback != null)
                callback.onConnectDevice(peerAgent.getAccessory().getName(), peerAgent.getPeerId(), peerAgent.getAccessory().getProductId());
        } else if (result == SAAgent.CONNECTION_ALREADY_EXIST) {
            if (callback != null)
                callback.onError(result);
        }
    }

    @Override
    protected void onPeerAgentsUpdated(SAPeerAgent[] saPeerAgents, int result) {
        //super.onPeerAgentsUpdated(saPeerAgents, i);
        Log.d(TAG, "onPeerAgentsUpdated " + Arrays.toString(saPeerAgents) + " " + result);
    }

    public class BTMProviderConnection extends SASocket {
        public BTMProviderConnection() {
            super(BTMProviderConnection.class.getName());
        }

        @Override
        public void onReceive(int channelId, byte[] data) {
            Log.d(TAG, "onReceive (" + channelId + "):\n" + new String(data));
            if (callback != null)
                callback.onReceive(channelId, data);
        }

        @Override
        public void onError(int channelId, String errorString, int error) {
            Log.d(TAG, "onError " + channelId + " " + errorString + " " + error);
            //callback.onError(error);
        }

        @Override
        public void onServiceConnectionLost(int errorCode) {
            Log.d(TAG, "onServiceConnectionLost " + errorCode);
            if (callback != null)
                callback.onServiceDisconnection();
            BTMProviderService.this.closeConnection();
        }
    }

    public interface StatusCallback {
        void onConnectDevice(String name, String peer, String product);

        void onError(int result);

        void onReceive(int channelId, byte[] data);

        void onServiceDisconnection();
    }
}
