package org.jssec.android.service.inhouseservice.messengeruser;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class InhouseMessengerUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;

    // Destination (Requested) service application information
    private static final String TARGET_PACKAGE =  "org.jssec.android.service.inhouseservice.messenger";
    private static final String TARGET_CLASS = "org.jssec.android.service.inhouseservice.messenger.InhouseMessengerService";

    // In-house signature permission
    private static final String MY_PERMISSION = "org.jssec.android.service.inhouseservice.messenger.MY_PERMISSION";

    // In-house certificate hash value
    private static String sMyCertHash = null;
    private static String myCertHash(Context context) {
        if (sMyCertHash == null) {
            if (Utils.isDebuggable(context)) {
                // Certificate hash value of debug.keystore "androiddebugkey"
                sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
            } else {
                // Certificate hash value of keystore "my company key"
                sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
            }
        }
        return sMyCertHash;
    }

    // Messenger used when this application receives data from service.
    private Messenger mServiceMessenger = null;

    // Messenger used when this application sends data to service.
    private final Messenger mActivityMessenger = new Messenger(new ActivitySideHandler());

    // Handler which handles message received from service
    private class ActivitySideHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonValue.MSG_SET_VALUE:
                    Bundle data = msg.getData();
                    String info = data.getString("key");
                    // *** POINT 13 *** Handle the received result data carefully and securely,
                    // even though the data came from an in-house application
                    // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
                    Toast.makeText(mContext, String.format("Received \"%s\" from service.", info),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // Connection used to connect with service. This is necessary when service is implemented with bindService(). 
    private ServiceConnection mConnection = new ServiceConnection() {

        // This is called when the connection with the service has been established.
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Toast.makeText(mContext, "Connect to service", Toast.LENGTH_SHORT).show();

            try {
                // Send own messenger to service
                Message msg = Message.obtain(null, CommonValue.MSG_REGISTER_CLIENT);
                msg.replyTo = mActivityMessenger;
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // Service stopped abnormally
            }
        }

        // This is called when the service stopped abnormally and connection is disconnected.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            mServiceMessenger = null;
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inhouseservice_activity);

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
        if (!mIsBound){
            // *** POINT 9 *** Verify that the in-house signature permission is defined by an in-house application.
            if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
                Toast.makeText(this, "In-house defined signature permission is not defined by in-house application.", Toast.LENGTH_LONG).show();
                return;
            }

            // *** POINT 10 *** Verify that the destination application is signed with the in-house certificate.
            if (!PkgCert.test(this, TARGET_PACKAGE, myCertHash(this))) {
                Toast.makeText(this, "Destination(Requested) service application is not in-house application.", Toast.LENGTH_LONG).show();
                return;
            }

          Intent intent = new Intent();

            // *** POINT 11 *** Sensitive information can be sent since the destination application is in-house one.
          intent.putExtra("PARAM", "Sensitive information");

            // *** POINT 12 *** Use the explicit intent to call an in-house service.
          intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);

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
        if (mServiceMessenger != null) {
            try {
                // Request sending information
                Message msg = Message.obtain(null, CommonValue.MSG_SET_VALUE);
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // Service stopped abnormally
            }
         }
    }
}
