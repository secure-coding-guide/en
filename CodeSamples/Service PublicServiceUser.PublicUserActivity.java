package org.jssec.android.service.publicserviceuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PublicUserActivity extends Activity {

    // Using Service Info
    private static final String TARGET_PACKAGE = "org.jssec.android.service.publicservice";
    private static final String TARGET_START_CLASS = "org.jssec.android.service.publicservice.PublicStartService";
    private static final String TARGET_INTENT_CLASS = "org.jssec.android.service.publicservice.PublicIntentService";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.publicservice_activity);
    }
    
    // --- StartService control ---
    
    public void onStartServiceClick(View v) {              
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // *** POINT 4 *** Call service by Explicit Intent
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);

        // *** POINT 5 *** Do not send sensitive information.
        intent.putExtra("PARAM", "Not sensitive information");

        startService(intent);
        // *** POINT 6 *** When receiving a result, handle the result data carefully and securely.
        // This sample code uses startService(), so receiving no result.
    }
    

    public void onStopServiceClick(View v) {
        doStopService();
    }
        
    // --- IntentService control ---

    public void onIntentServiceClick(View v) {      
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.intentservice");

        // *** POINT 4 *** Call service by Explicit Intent
        intent.setClassName(TARGET_PACKAGE, TARGET_INTENT_CLASS);

        // *** POINT 5 *** Do not send sensitive information.
        intent.putExtra("PARAM", "Not sensitive information");

        startService(intent);
    }
        
    @Override
    public void onStop(){
        super.onStop();
        // Stop service if the service is running.
        doStopService();
    }
    
    // Stop service
    private void doStopService() {            
        Intent intent = new Intent("org.jssec.android.service.publicservice.action.startservice");

        // *** POINT 4 *** Call service by Explicit Intent
        intent.setClassName(TARGET_PACKAGE, TARGET_START_CLASS);

        stopService(intent);        
    }
}
