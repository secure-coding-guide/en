package org.jssec.android.activity.partneruser;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PartnerUserActivity extends Activity {

    // *** POINT 7 *** Verify if the certificate of a target application has been registered in a white list.
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();
        
        // Register the certificate hash value of partner application org.jssec.android.activity.partneractivity.
        sWhitelists.add("org.jssec.android.activity.partneractivity", isdebug ?
                // The certificate hash value of "androiddebugkey" is in debug.keystore.
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // The certificate hash value of "my company key" is in the keystore.
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");
        
        // Register the other partner applications in the same way.
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    
    private static final int REQUEST_CODE = 1;

    // Information related the target partner activity
    private static final String TARGET_PACKAGE =  "org.jssec.android.activity.partneractivity";
    private static final String TARGET_ACTIVITY = "org.jssec.android.activity.partneractivity.PartnerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onUseActivityClick(View view) {

        // *** POINT 7 *** Verify if the certificate of the target application has been registered in the own white list.
        if (!checkPartner(this, TARGET_PACKAGE)) {
            Toast.makeText(this, "Target application is not a partner application.", Toast.LENGTH_LONG).show();
            return;
        }
        
        try {
            // *** POINT 8 *** Do not set the FLAG_ACTIVITY_NEW_TASK flag for the intent that start an activity.
            Intent intent = new Intent();
            
            // *** POINT 9 *** Only send information that is granted to be disclosed to a Partner Activity only by putExtra().
            intent.putExtra("PARAM", "Info for Partner Apps");
            
            // *** POINT 10 *** Use explicit intent to call a Partner Activity.
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            
            // *** POINT 11 *** Use startActivityForResult() to call a Partner Activity.
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Target activity not found.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        
        switch (requestCode) {
        case REQUEST_CODE:
            String result = data.getStringExtra("RESULT");
            
            // *** POINT 12 *** Handle the received data carefully and securely,
            // even though the data comes from a partner application.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            Toast.makeText(this,
                    String.format("Received result: \"%s\"", result), Toast.LENGTH_LONG).show();
            break;
        }
    }
}
