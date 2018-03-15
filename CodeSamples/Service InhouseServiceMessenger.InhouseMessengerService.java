package org.jssec.android.service.inhouseservice.messenger;

import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class InhouseMessengerService extends Service{
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
    
    
    // Manage clients(destinations of sending data) in a list
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    
    // Messenger used when service receive data from client
    private final Messenger mMessenger = new Messenger(new ServiceSideHandler(mClients));
    
    // Handler which handles message received from client
    private static class ServiceSideHandler extends Handler{

        private ArrayList<Messenger> mClients;

        public ServiceSideHandler(ArrayList<Messenger> clients){
            mClients = clients;
        }

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case CommonValue.MSG_REGISTER_CLIENT:
                // Add messenger received from client
                mClients.add(msg.replyTo);
                break;
            case CommonValue.MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            case CommonValue.MSG_SET_VALUE:
                // Send data to client
                sendMessageToClients(mClients);
                break;
            default:
                super.handleMessage(msg);
               break;
            }
        }
    }
    
    /**
     * Send data to client
     */
    private static void sendMessageToClients(ArrayList<Messenger> mClients){
        
        // *** POINT 6 *** Sensitive information can be returned since the requesting application is in-house.
        String sendValue = "Sensitive information (from Service)";
        
        // Send data to the registered client one by one.
        // Use iterator to send all clients even though clients are removed in the loop process.  
        Iterator<Messenger> ite = mClients.iterator();
        while(ite.hasNext()){
            try {
                Message sendMsg = Message.obtain(null, CommonValue.MSG_SET_VALUE, null);

                Bundle data = new Bundle();
                data.putString("key", sendValue);
                sendMsg.setData(data);

                Messenger next = ite.next();
                next.send(sendMsg);
                
            } catch (RemoteException e) {
                // If client does not exits, remove it from a list.
                ite.remove();
            }
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        
        // *** POINT 4 *** Verify that the in-house signature permission is defined by an in-house application.
        if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
            Toast.makeText(this, "In-house defined signature permission is not defined by in-house application.", Toast.LENGTH_LONG).show();
            return null;
        }

        // *** POINT 5 *** Handle the received intent carefully and securely,
        // even though the intent was sent from an in-house application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("Received parameter \"%s\".", param), Toast.LENGTH_LONG).show();

        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service - onDestroy()", Toast.LENGTH_SHORT).show();
    }
}
