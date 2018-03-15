package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TemporaryActiveGrantActivity extends Activity {

    // User Activity Information
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryuser";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryuser.TemporaryUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_grant);
    }

    // In the case that Content Provider application grants access permission to other application actively.
    public void onSendClick(View view) {
        try {
            Intent intent = new Intent();

            // *** POINT 5 *** Specify URI for the intent to grant temporary access.
            intent.setData(TemporaryProvider.Address.CONTENT_URI);

            // *** POINT 6 *** Specify access rights for the intent to grant temporary access.
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // *** POINT 7 *** Send the explicit intent to an application to grant temporary access.
            intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "User Activity not found.", Toast.LENGTH_LONG).show();
        }
    }
}
