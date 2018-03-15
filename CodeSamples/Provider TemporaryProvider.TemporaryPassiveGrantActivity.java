package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TemporaryPassiveGrantActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passive_grant);
    }

    // In the case that Content Provider application passively grants access permission
    // to the application that requested Content Provider access.
    public void onGrantClick(View view) {
        Intent intent = new Intent();

        // *** POINT 5 *** Specify URI for the intent to grant temporary access.
        intent.setData(TemporaryProvider.Address.CONTENT_URI);

        // *** POINT 6 *** Specify access rights for the intent to grant temporary access.
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // *** POINT 8 *** Return the intent to the application that requests temporary access.
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCloseClick(View view) {
        finish();
    }
}
