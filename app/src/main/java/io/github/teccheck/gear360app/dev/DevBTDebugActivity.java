package io.github.teccheck.gear360app.dev;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import io.github.teccheck.gear360app.R;
import io.github.teccheck.gear360app.Utils;
import io.github.teccheck.gear360app.bluetooth.BTMessageLogger;

public class DevBTDebugActivity extends AppCompatActivity {

    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_btdebug);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.messages);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        BTMessageAdapter adapter = new BTMessageAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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

    public class BTMessageAdapter extends RecyclerView.Adapter<BTMessageAdapter.ViewHolder> {

        public final String TAG = "G360_" + BTMessageAdapter.class.getSimpleName();

        @Override
        public void onBindViewHolder(BTMessageAdapter.ViewHolder holder, int position) {

            Log.d(TAG, "onBindViewHolder " + position);

            CardView cardView = holder.cardView;
            TextView text = cardView.findViewById(R.id.text);
            TextView time = cardView.findViewById(R.id.time);
            BTMessageLogger.LoggerMessage message = BTMessageLogger.getMessages().get(position);

            text.setText(Utils.getPrettyJsonStringFromByteData(message.message.toString()));
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTimeInMillis(message.time);
            time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

            if(message.outgoing){
                cardView.setCardBackgroundColor(0x3B7DB931);
            }else {
                cardView.setCardBackgroundColor(0xFFFFFFFF);
            }
        }

        @Override
        public int getItemCount() {
            return BTMessageLogger.getMessages().size();
        }

        @Override
        public BTMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_logger_item_in, parent, false);
            return new BTMessageAdapter.ViewHolder(cardView);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;

            ViewHolder(CardView view) {
                super(view);
                cardView = view;
            }
        }
    }
}
