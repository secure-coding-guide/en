package org.jssec.android.activity.privateactivity;

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
        
        // *** POINT 6 *** Do not set the FLAG_ACTIVITY_NEW_TASK flag for intents to start an activity.
        // *** POINT 7 *** Use the explicit Intents with the class specified to call an activity in the same application.
        Intent intent = new Intent(this, PrivateActivity.class);
        
        // *** POINT 8 *** Sensitive information can be sent only by putExtra() since the destination activity is in the same application. 
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
            
            // *** POINT 9 *** Handle the received data carefully and securely,
            // even though the data comes from an activity within the same application.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            Toast.makeText(this, String.format("Received result: \"%s\"", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
