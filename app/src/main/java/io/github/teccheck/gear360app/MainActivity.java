package io.github.teccheck.gear360app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;

import io.github.teccheck.gear360app.bluetooth.BTMessageSender;
import io.github.teccheck.gear360app.bluetooth.BTService;
import io.github.teccheck.gear360app.dev.DevHomeActivity;
import io.github.teccheck.gear360app.ui.ConfigEntryAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "G360_" + MainActivity.class.getSimpleName();
    public static BTService service = null;

    TextView cameraName = null;
    TextView versionCode = null;
    TextView versionName = null;
    TextView mac = null;
    Button connectBtn = null;
    Button sendShot = null;
    Button send2 = null;
    Button refreshList = null;

    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBtn = findViewById(R.id.connectBtn);
        sendShot = findViewById(R.id.sendShot);
        send2 = findViewById(R.id.send2);
        refreshList = findViewById(R.id.refresh);
        cameraName = findViewById(R.id.cameraName);
        versionCode = findViewById(R.id.versionCode);
        versionName = findViewById(R.id.versionName);
        mac = findViewById(R.id.mac);
        recyclerView = findViewById(R.id.recycler_view);

        ConfigEntryAdapter adapter = new ConfigEntryAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        try {
            SA accessory = new SA();
            accessory.initialize(getApplicationContext());
            versionName.setText("Version: " + accessory.getVersionName());
            versionCode.setText("Code: " + accessory.getVersionCode());
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        if(service == null) {
            service = new BTService();
            service.start(this);
        }

        connectBtn.setOnClickListener(this);
        sendShot.setOnClickListener(this);
        send2.setOnClickListener(this);
        refreshList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectBtn:
                service.connect();
                break;
            case R.id.sendShot:
                BTMessageSender.sendShotRequest(service);
                break;
            case R.id.send2:
                startActivity(new Intent(this, DevHomeActivity.class));
                break;
            case R.id.refresh:
                BTMessageSender.sendPhoneInfo(service);
                updateList();
                break;
        }
    }

    public void setCameraName(String deviceName) {
        cameraName.setText(deviceName);
    }

    public void setCameraMac(String mac) {
        this.mac.setText(mac);
    }

    public void updateList() {
        ConfigEntryAdapter adapter = new ConfigEntryAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
