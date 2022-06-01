package io.github.teccheck.gear360app.dev;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.github.teccheck.gear360app.R;
import io.github.teccheck.gear360app.ui.BaseMenuEntryAdapter;
import io.github.teccheck.gear360app.ui.RecyclerItemClickListener;

public class DevHomeActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_home);
        recyclerView = findViewById(R.id.dev_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        BaseMenuEntryAdapter adapter = new BaseMenuEntryAdapter();
        adapter.addItem("Bluetooth Debugger", "Debug the Bluetooth communication", getDrawable(R.drawable.ic_bluetooth_connected_black_24dp));
        adapter.addItem("Gear360 State", "See the Gears State and Settings", getDrawable(R.drawable.ic_photo_camera_black_24dp));
        adapter.addItem("Functions", "Test different functions", getDrawable(R.drawable.ic_apps_black_24dp));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerItemClickListener listener = new RecyclerItemClickListener(getApplicationContext(), recyclerView, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(listener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                //Toast.makeText(getApplicationContext(), "BT", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DevBTDebugActivity.class));
                break;
            case 1:
                Toast.makeText(getApplicationContext(), "Gear", Toast.LENGTH_LONG).show();
                break;
            case 2:
                startActivity(new Intent(this, DevFunctions.class));
                break;
        }
    }

    @Override
    public void onLongItemClick(View view, int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
