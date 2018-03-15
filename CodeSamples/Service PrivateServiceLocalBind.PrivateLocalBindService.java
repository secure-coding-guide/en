package org.jssec.android.service.privateservice.localbind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateLocalBindService extends Service
implements IPrivateLocalBindService{   
    /**
     * Class to connect service
     */
    public class LocalBinder extends Binder {
        PrivateLocalBindService getService() {
            return PrivateLocalBindService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // *** POINT 3 *** Handle intent carefully and securely,
        // even though the intent came from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("Received parameter \"%s\".", param), Toast.LENGTH_LONG).show();
        
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    // Interface
    @Override
    public String getInfo() {
        // *** POINT 5 *** Sensitive information can be returned since the requesting application is in the same application.       
        return "Sensitive information (from Service)";
    }
}
