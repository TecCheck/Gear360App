package de.teccheck.gear360app.dev;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.teccheck.gear360app.LiveTestActivity;
import de.teccheck.gear360app.MainActivity;
import de.teccheck.gear360app.R;
import de.teccheck.gear360app.Utils;
import de.teccheck.gear360app.bluetooth.BTMessageSender;

public class DevFunctions extends AppCompatActivity implements View.OnClickListener {

    Button liveView = null;
    Button liveView2 = null;
    Button wifiConnect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_functions);

        liveView = findViewById(R.id.live);
        liveView.setOnClickListener(this);

        liveView2 = findViewById(R.id.live_activity);
        liveView2.setOnClickListener(this);

        wifiConnect = findViewById(R.id.wifi_connect);
        wifiConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.live:
                BTMessageSender.sendLiveViewRequest(MainActivity.service);
                break;
            case R.id.live_activity:
                startActivity(new Intent(this, LiveTestActivity.class));
                break;
            case R.id.wifi_connect:
                Utils.connectWifi(getApplicationContext());
                break;
        }
    }
}
