package org.jssec.android.service.privateservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateStartService extends Service {
    
    // The onCreate gets called only one time when the service starts.
    @Override
    public void onCreate() {
        Toast.makeText(this, "PrivateStartService - onCreate()", Toast.LENGTH_SHORT).show();
    }

    // The onStartCommand gets called each time after the startService gets called.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // *** POINT 2 *** Handle the received intent carefully and securely,
        // even though the intent was sent from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this,
                String.format("PrivateStartService\nReceived param: \"%s\"", param),
                Toast.LENGTH_LONG).show();

        return Service.START_NOT_STICKY;
    }

    // The onDestroy gets called only one time when the service stops.
    @Override
    public void onDestroy() {
        Toast.makeText(this, "PrivateStartService - onDestroy()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // This service does not provide binding, so return null
        return null;
    }
}
