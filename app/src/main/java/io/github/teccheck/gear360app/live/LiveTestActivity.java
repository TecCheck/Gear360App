package io.github.teccheck.gear360app.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;

import io.github.teccheck.gear360app.R;

public class LiveTestActivity extends AppCompatActivity {

    private static final String TAG = "LiveTestActivity";

    SurfaceView surfaceView = null;
    VideoThread videoThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test);

        surfaceView = findViewById(R.id.surfaceView);
        Surface surface = surfaceView.getHolder().getSurface();
        String file = "http://192.168.107.1:7679/livestream_high.avi";

        videoThread = new VideoThread();
        videoThread.init(surface, file);
        videoThread.setName("Video Thread");
        videoThread.start();
    }

    @Override
    public void onBackPressed() {
        if (videoThread != null) {
            videoThread.releaseEveryThing();
            videoThread.interrupt();
            videoThread = null;
            //videoThread.stop();
        }

        super.onBackPressed();
    }
}