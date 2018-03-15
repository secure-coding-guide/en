package org.jssec.android.log.outputredirection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LogActivity extends Activity {

    final static String LOG_TAG = "LogActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        
        Log.i(LOG_TAG,"Output logs by Log.i() (1st time)");
        System.out.println("output logs to System.out");    // Not output logs in release version.
        System.err.println("output logs to System.err");    // Not output logs in release version.
        Log.i(LOG_TAG,"Output logs by Log.i() (2nd time)");
    }
}
