package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PrivateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        
        // *** POINT 2 *** Handle the received intent carefully and securely,
        // even though the intent was sent from within the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(context,
                String.format("Received param: \"%s\"", param),
                Toast.LENGTH_SHORT).show();
        
        // *** POINT 3 *** Sensitive information can be sent as the returned results since the requests come from within the same application.
        setResultCode(Activity.RESULT_OK);
        setResultData("Sensitive Info from Receiver");
        abortBroadcast();
    }
}
