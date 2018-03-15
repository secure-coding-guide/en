package org.jssec.android.service.partnerservice.aidl;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

public class PartnerAIDLService extends Service {
       private static final int REPORT_MSG = 1;
       private static final int GETINFO_MSG = 2;

    // The value which this service informs to client
    private int mValue = 0;

    // *** POINT 2 *** Verify that the certificate of the requesting application has been registered in the own white list.
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();

        // Register certificate hash value of partner application "org.jssec.android.service.partnerservice.aidluser"
        sWhitelists.add("org.jssec.android.service.partnerservice.aidluser", isdebug ?
                // Certificate hash value of  debug.keystore "androiddebugkey"
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // Certificate hash value of keystore "partner key"
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");

        // Register other partner applications in the same way
    }

    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }

    // Object to register callback
    // Methods which RemoteCallbackList provides are thread-safe.
    private final RemoteCallbackList<IPartnerAIDLServiceCallback> mCallbacks =
        new RemoteCallbackList<IPartnerAIDLServiceCallback>();

    // Handler to send data when callback is called.
    private static class ServiceHandler extends Handler{

        private Context mContext;
        private RemoteCallbackList<IPartnerAIDLServiceCallback> mCallbacks;
        private int mValue = 0;

        public ServiceHandler(Context context, RemoteCallbackList<IPartnerAIDLServiceCallback> callback, int value){
            this.mContext = context;
            this.mCallbacks = callback;
            this.mValue = value;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case REPORT_MSG: {
                if(mCallbacks == null){
                    return;
                }
                // Start broadcast
                // To call back on to the registered clients, use beginBroadcast().
                // beginBroadcast() makes a copy of the currently registered callback list.
                final int N = mCallbacks.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    IPartnerAIDLServiceCallback target = mCallbacks.getBroadcastItem(i);
                    try {
                        // *** POINT 5 *** Information that is granted to disclose to partner applications can be returned.
                        target.valueChanged("Information disclosed to partner application (callback from Service) No." + (++mValue));

                    } catch (RemoteException e) {
                        // Callbacks are managed by RemoteCallbackList, do not unregister callbacks here.
                        // RemoteCallbackList.kill() unregister all callbacks
                    }
                }
                // finishBroadcast() cleans up the state of a broadcast previously initiated by calling beginBroadcast().
                mCallbacks.finishBroadcast();

                // Repeat after 10 seconds
                sendEmptyMessageDelayed(REPORT_MSG, 10000);
                break;
             }
            case GETINFO_MSG: {
                if(mContext != null) {
                    Toast.makeText(mContext,
                            (String) msg.obj, Toast.LENGTH_LONG).show();
                }
                break;
              }
            default:
                super.handleMessage(msg);
                break;
            } // switch
        }
    }

    protected final ServiceHandler mHandler = new ServiceHandler(this, mCallbacks, mValue);

    // Interfaces defined in AIDL
    private final IPartnerAIDLService.Stub mBinder = new IPartnerAIDLService.Stub() {
        private boolean checkPartner() {
            Context ctx = PartnerAIDLService.this;
            if (!PartnerAIDLService.checkPartner(ctx, Utils.getPackageNameFromUid(ctx, getCallingUid()))) {
                mHandler.post(new Runnable(){
                    @Override
                    public void run(){
                       Toast.makeText(PartnerAIDLService.this, "Requesting application is not partner application.", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
           return true;
        }
        public void registerCallback(IPartnerAIDLServiceCallback cb) {
            // *** POINT 2 *** Verify that the certificate of the requesting application has been registered in the own white list.
          if (!checkPartner()) {
                return;
            }
          if (cb != null) mCallbacks.register(cb);
        }
        public String getInfo(String param) {
            // *** POINT 2 *** Verify that the certificate of the requesting application has been registered in the own white list.
            if (!checkPartner()) {
                return null;
            }
            // *** POINT 4 *** Handle the received intent carefully and securely,
            // even though the intent was sent from a partner application
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            Message msg = new Message();
            msg.what = GETINFO_MSG;
            msg.obj = String.format("Method calling from partner application. Recieved \"%s\"", param);
            PartnerAIDLService.this.mHandler.sendMessage(msg);

            // *** POINT 5 *** Return only information that is granted to be disclosed to a partner application.
            return "Information disclosed to partner application (method from Service)";
        }

        public void unregisterCallback(IPartnerAIDLServiceCallback cb) {
            // *** POINT 2 *** Verify that the certificate of the requesting application has been registered in the own white list.
           if (!checkPartner()) {
                return;
            }

           if (cb != null) mCallbacks.unregister(cb);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // *** POINT 3 *** Verify that the certificate of the requesting application has been registered in the own white list.
        // So requesting application must be validated in methods defined in AIDL every time.
        return mBinder;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();

        // During service is running, inform the incremented number periodically.
        mHandler.sendEmptyMessage(REPORT_MSG);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();

        // Unregister all callbacks
        mCallbacks.kill();

        mHandler.removeMessages(REPORT_MSG);
    }
}
