package org.jssec.android.service.privateservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrivateUserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privateservice_activity);
    }

    // --- StartService control ---
    
    public void onStartServiceClick(View v) {
        // *** POINT 4 *** Use the explicit intent with class specified to call a service in the same application.
        Intent intent = new Intent(this, PrivateStartService.class);

        // *** POINT 5 *** Sensitive information can be sent since the destination service is in the same application.
        intent.putExtra("PARAM", "Sensitive information");

        startService(intent);
    }

    public void onStopServiceClick(View v) {
        doStopService();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop service if the service is running.
        doStopService();
    }

    private void doStopService() {
        // *** POINT 4 *** Use the explicit intent with class specified to call a service in the same application.
        Intent intent = new Intent(this, PrivateStartService.class);
        stopService(intent);
    }

    // --- IntentService control ---

    public void onIntentServiceClick(View v) {
        // *** POINT 4 *** Use the explicit intent with class specified to call a service in the same application.
        Intent intent = new Intent(this, PrivateIntentService.class);
          
        // *** POINT 5 *** Sensitive information can be sent since the destination service is in the same application.
        intent.putExtra("PARAM", "Sensitive information");

        startService(intent);
    }
}
