package org.jssec.android.broadcast.inhousereceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class DynamicReceiverService extends Service {

    private static final String MY_BROADCAST_INHOUSE =
        "org.jssec.android.broadcast.MY_BROADCAST_INHOUSE";

    private InhouseReceiver mReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mReceiver = new InhouseReceiver();
        mReceiver.isDynamic = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_BROADCAST_INHOUSE);
        filter.setPriority(1);  // Prioritize Dynamic Broadcast Receiver, rather than Static Broadcast Receiver.

        // *** POINT 5 *** When registering a dynamic broadcast receiver, require the in-house signature permission.
        registerReceiver(mReceiver, filter, "org.jssec.android.broadcast.inhousereceiver.MY_PERMISSION", null);

        Toast.makeText(this,
                "Registered Dynamic Broadcast Receiver.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mReceiver = null;
        Toast.makeText(this,
                "Unregistered Dynamic Broadcast Receiver.",
                Toast.LENGTH_SHORT).show();
    }
}
