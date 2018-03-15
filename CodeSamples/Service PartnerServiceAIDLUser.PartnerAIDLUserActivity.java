package org.jssec.android.service.partnerservice.aidluser;

import org.jssec.android.service.partnerservice.aidl.IPartnerAIDLService;
import org.jssec.android.service.partnerservice.aidl.IPartnerAIDLServiceCallback;
import org.jssec.android.shared.PkgCertWhitelists;
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
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class PartnerAIDLUserActivity extends Activity {

    private boolean mIsBound;
    private Context mContext;
    
    private final static int MGS_VALUE_CHANGED = 1;
    
    // *** POINT 6 *** Verify if the certificate of the target application has been registered in the own white list.
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // Register certificate hash value of partner service application "org.jssec.android.service.partnerservice.aidl"
        sWhitelists.add("org.jssec.android.service.partnerservice.aidl", isdebug ?
                // Certificate hash value of debug.keystore "androiddebugkey"
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // Certificate hash value of  keystore "my company key"
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");
        
        // Register other partner service applications in the same way
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }

    // Information about destination (requested) partner activity.
    private static final String TARGET_PACKAGE =  "org.jssec.android.service.partnerservice.aidl";
    private static final String TARGET_CLASS = "org.jssec.android.service.partnerservice.aidl.PartnerAIDLService";

    private static class ReceiveHandler extends Handler{

        private Context mContext;

        public ReceiveHandler(Context context){
            this.mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MGS_VALUE_CHANGED: {
                      String info = (String)msg.obj;
                    Toast.makeText(mContext, String.format("Received \"%s\" with callback.", info), Toast.LENGTH_SHORT).show();
                    break;
                  }
                default:
                    super.handleMessage(msg);
                    break;
           } // switch
        }    
    }

    private final ReceiveHandler mHandler = new ReceiveHandler(this);
    
    // Interfaces defined in AIDL. Receive notice from service
    private final IPartnerAIDLServiceCallback.Stub mCallback =
        new IPartnerAIDLServiceCallback.Stub() {
            @Override
            public void valueChanged(String info) throws RemoteException {
                Message msg = mHandler.obtainMessage(MGS_VALUE_CHANGED, info);
                mHandler.sendMessage(msg);
            }
    };
    
    // Interfaces defined in AIDL. Inform service.
    private IPartnerAIDLService mService = null;
    
    // Connection used to connect with service. This is necessary when service is implemented with bindService(). 
    private ServiceConnection mConnection = new ServiceConnection() {

        // This is called when the connection with the service has been established.
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = IPartnerAIDLService.Stub.asInterface(service);
            
            try{
                // connect to service
                mService.registerCallback(mCallback);
                
            }catch(RemoteException e){
                // service stopped abnormally
            }
            
            Toast.makeText(mContext, "Connected to service", Toast.LENGTH_SHORT).show();
        }

        // This is called when the service stopped abnormally and connection is disconnected.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Toast.makeText(mContext, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.partnerservice_activity);

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
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * Connect to service
     */
    private void doBindService() {
        if (!mIsBound){
            // *** POINT 6 *** Verify if the certificate of the target application has been registered in the own white list.
            if (!checkPartner(this, TARGET_PACKAGE)) {
                Toast.makeText(this, "Destination(Requested) sevice application is not registered in white list.", Toast.LENGTH_LONG).show();
                return;
            }
            
            Intent intent = new Intent();
            
            // *** POINT 7 *** Return only information that is granted to be disclosed to a partner application.
            intent.putExtra("PARAM", "Information disclosed to partner application");
            
            // *** POINT 8 *** Use the explicit intent to call a partner service.
            intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);
                 
          bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
          mIsBound = true;
        }
    }

    /**
     * Disconnect service
     */
    private void doUnbindService() {
        if (mIsBound) {
            // Unregister callbacks which have been registered. 
            if(mService != null){
                try{
                    mService.unregisterCallback(mCallback);
                }catch(RemoteException e){
                    // Service stopped abnormally
                    // Omitted, since it' s sample.
                }
            }
            
          unbindService(mConnection);
            
          Intent intent = new Intent();
        
        // *** POINT 8 *** Use the explicit intent to call a partner service.
          intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);
 
          stopService(intent);
          
          mIsBound = false;
        }
    }

    /**
     * Get information from service
     */
    void getServiceinfo() {
        if (mIsBound && mService != null) {
            String info = null;
            
            try {
                // *** POINT 7 *** Return only information that is granted to be disclosed to a partner application.
                info = mService.getInfo("Information disclosed to partner application (method from activity)");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // *** POINT 9 *** Handle the received result data carefully and securely,
            // even though the data came from a partner application.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            Toast.makeText(mContext, String.format("Received \"%s\" from service.", info), Toast.LENGTH_SHORT).show();
         }
    }
}
