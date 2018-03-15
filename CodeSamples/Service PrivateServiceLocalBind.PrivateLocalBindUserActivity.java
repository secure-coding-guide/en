package org.jssec.android.service.privateservice.localbind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class PrivateLocalBindUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;

    // Interface implemented in service is defined as IPrivateLocalBindService class.
    private IPrivateLocalBindService mServiceInterface;
    
    // Connection used to connect with service. This is necessary when service is implemented with bindService(). 
    private ServiceConnection mConnection = new ServiceConnection() {

        // This is called when the connection with the service has been established.
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceInterface = ((PrivateLocalBindService.LocalBinder)service).getService();
            Toast.makeText(mContext, "Connect to service", Toast.LENGTH_SHORT).show();
        }
        // This is called when the service stopped abnormally and connection is disconnected.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            // Set null since service cannot be used.
            mServiceInterface = null;
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.privateservice_activity);

        mContext = this;
    }

    // --- StartService control ---
    
    public void onStartServiceClick(View v) {
        // Start bindService
        doBindService();
    }
    
    public void onGetInfoClick(View v) {
        getServiceinfo();
    }
    
    public void onStopServiceClick(View v) {
        doUnbindService();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * Connect to service
     */
    void doBindService() {
       if (!mIsBound)
        {
           // *** POINT 1 *** Services in the same application must be called by explicit intent with specified class.
           Intent intent = new Intent(this, PrivateLocalBindService.class);
           
           // *** POINT 2 *** Sensitive information can be sent since the service is in the same application.
           intent.putExtra("PARAM", "Sensitive information(from activity)");
            
           bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
           mIsBound = true;
        }
    }

    /**
     * Disconnect service
     */
    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    /**
     * Get information from service
     */
    void getServiceinfo() {
        if (mIsBound) {
            String info = mServiceInterface.getInfo();
            
            Toast.makeText(mContext, String.format("Received \"%s\" from service.", info), Toast.LENGTH_SHORT).show();
         }
    }
}
