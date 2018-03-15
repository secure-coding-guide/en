package org.jssec.android.service.publicservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PublicStartService extends Service{

    // The onCreate gets called only one time when the Service starts.
    @Override
    public void onCreate() {     
        Toast.makeText(this, this.getClass().getSimpleName() + " - xxonCreate()", Toast.LENGTH_SHORT).show();
    }

    
    // The onStartCommand gets called each time after the startService gets called.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // *** POINT 2 *** Handle intent carefully and securely.
        // Since it's public service, the intent may come from malicious application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("Recieved parameter \"%s\"", param), Toast.LENGTH_LONG).show();
        
        // *** POINT 3 *** When returning a result, do not include sensitive information
        // This sample code uses startService(), so returning no result.
                
        // The service must be stopped explicitly.
        // When stopSelf() or stopService() are called, stop the service is stopped.
        // If this method returns START_NOT_STICKY, this service will not resume automatically when it be killed in case of shortage memory.
        return Service.START_NOT_STICKY;
    }
    
    // The onDestroy gets called only one time when the service stops.
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // This service does not provide binding, so return null
        return null;
    }
}
