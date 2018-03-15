package org.jssec.android.broadcast.publicsender;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PublicSenderActivity extends Activity {
    
    private static final String MY_BROADCAST_PUBLIC =
        "org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
    
    public void onSendNormalClick(View view) {
        // *** POINT 4 *** Do not send sensitive information.
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "Not Sensitive Info from Sender");
        sendBroadcast(intent);
    }
    
    public void onSendOrderedClick(View view) {
        // *** POINT 4 *** Do not send sensitive information.
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "Not Sensitive Info from Sender");
        sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
    }
    
    public void onSendStickyClick(View view) {
        // *** POINT 4 *** Do not send sensitive information.
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "Not Sensitive Info from Sender");
        //sendStickyBroadcast is deprecated at API Level 21
        sendStickyBroadcast(intent);
    }

    public void onSendStickyOrderedClick(View view) {
        // *** POINT 4 *** Do not send sensitive information.
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        intent.putExtra("PARAM", "Not Sensitive Info from Sender");
        //sendStickyOrderedBroadcast is deprecated at API Level 21
        sendStickyOrderedBroadcast(intent, mResultReceiver, null, 0, null, null);
    }
    
    public void onRemoveStickyClick(View view) {
        Intent intent = new Intent(MY_BROADCAST_PUBLIC);
        //removeStickyBroadcast is deprecated at API Level 21
        removeStickyBroadcast(intent);
    }

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            // *** POINT 5 *** When receiving a result, handle the result data carefully and securely.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            String data = getResultData();
            PublicSenderActivity.this.logLine(
                    String.format("Received result: \"%s\"", data));
        }
    };

    private TextView mLogView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }
    
    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
