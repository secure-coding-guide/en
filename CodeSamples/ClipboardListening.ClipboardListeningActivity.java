package org.jssec.android.clipboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ClipboardListeningActivity extends Activity {
    private static final String TAG = "ClipboardListeningActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard_listening);
    }

    public void onClickStartService(View view) {
        if (view.getId() != R.id.start_service_button) {
            Log.w(TAG, "View ID is incorrect.");
        } else {
            ComponentName cn = startService(
                new Intent(ClipboardListeningActivity.this, ClipboardListeningService.class));
            if (cn == null) {
                Log.e(TAG, "Failed to launch the service.");
            }
        }
    }

    public void onClickStopService(View view) {
        if (view.getId() != R.id.stop_service_button) {
            Log.w(TAG, "View ID is incorrect.");
        } else {
            stopService(new Intent(ClipboardListeningActivity.this, ClipboardListeningService.class));
        }
    }
}
