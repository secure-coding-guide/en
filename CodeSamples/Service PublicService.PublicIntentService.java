package org.jssec.android.service.publicservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PublicIntentService extends IntentService{

    /**
     * Default constructor must be provided when a service extends IntentService class.
     * If it does not exist, an error occurs.
     */
    public PublicIntentService() {
        super("CreatingTypeBService");
    }

    // The onCreate gets called only one time when the Service starts.
    @Override
    public void onCreate() {
        super.onCreate();
        
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    // The onHandleIntent gets called each time after the startService gets called.
    @Override
    protected void onHandleIntent(Intent intent) {        
        // *** POINT 2 *** Handle intent carefully and securely.
        // Since it's public service, the intent may come from malicious application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("Recieved parameter \"%s\"", param), Toast.LENGTH_LONG).show();
    }


    // The onDestroy gets called only one time when the service stops.
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
