package org.jssec.android.activity.singleinstanceactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateUserActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
    }
    
    public void onUseActivityClick(View view) {
        
        // Start the Private Activity with "standard" lanchMode.
        Intent intent = new Intent(this, PrivateActivity.class);
        intent.putExtra("PARAM", "Sensitive Info");
        
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        
        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");
            
            // Handle received result data carefully and securely,
            // even though the data came from the Activity in the same application.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            Toast.makeText(this, String.format("Received result: \"%s\"", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
