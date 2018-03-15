package org.jssec.android.provider.partneruser;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PartnerUserActivity extends Activity {

    // Target Content Provider Information
    private static final String AUTHORITY = "org.jssec.android.provider.partnerprovider";
    private interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    // *** POINT 4 *** Verify if the certificate of the target application has been registered in the own white list.
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();

        // Register certificate hash value of partner application org.jssec.android.provider.partnerprovider.
        sWhitelists.add("org.jssec.android.provider.partnerprovider", isdebug ?
                // Certificate hash value of "androiddebugkey" in the debug.keystore.
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // Certificate hash value of "partner key" in the keystore.
                "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA");
        
        // Register following other partner applications in the same way.
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    
    // Get package name of target content provider.
    private String providerPkgname(Uri uri) {
        String pkgname = null;
        ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
        if (pi != null) pkgname = pi.packageName;
        return pkgname;
    }

    public void onQueryClick(View view) {

        logLine("[Query]");

        // *** POINT 4 *** Verify if the certificate of the target application has been registered in the own white list.
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  The target content provider is not served by partner applications.");
            return;
        }

        Cursor cursor = null;
        try {
            // *** POINT 5 *** Information that is granted to disclose to partner applications can be sent.
            cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // *** POINT 6 *** Handle the received result data carefully and securely,
            // even though the data comes from a partner application.
            // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
            if (cursor == null) {
                logLine("  null cursor");
            } else {
                boolean moved = cursor.moveToFirst();
                while (moved) {
                    logLine(String.format("  %d, %s", cursor.getInt(0), cursor.getString(1)));
                    moved = cursor.moveToNext();
                }
            }
        }
        finally {
            if (cursor != null) cursor.close();
        }
    }

    public void onInsertClick(View view) {

        logLine("[Insert]");

        // *** POINT 4 *** Verify if the certificate of the target application has been registered in the own white list.
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  The target content provider is not served by partner applications.");
            return;
        }

        // *** POINT 5 *** Information that is granted to disclose to partner applications can be sent.
        ContentValues values = new ContentValues();
        values.put("city", "Tokyo");
        Uri uri = getContentResolver().insert(Address.CONTENT_URI, values);

        // *** POINT 6 *** Handle the received result data carefully and securely,
        // even though the data comes from a partner application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // *** POINT 4 *** Verify if the certificate of the target application has been registered in the own white list.
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  The target content provider is not served by partner applications.");
            return;
        }

        // *** POINT 5 *** Information that is granted to disclose to partner applications can be sent.
        ContentValues values = new ContentValues();
        values.put("city", "Tokyo");
        String where = "_id = ?";
        String[] args = { "4" };
        int count = getContentResolver().update(Address.CONTENT_URI, values, where, args);

        // *** POINT 6 *** Handle the received result data carefully and securely,
        // even though the data comes from a partner application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // *** POINT 4 *** Verify if the certificate of the target application has been registered in the own white list.
        if (!checkPartner(this, providerPkgname(Address.CONTENT_URI))) {
            logLine("  The target content provider is not served by partner applications.");
            return;
        }

        // *** POINT 5 *** Information that is granted to disclose to partner applications can be sent.
        int count = getContentResolver().delete(Address.CONTENT_URI, null, null);

        // *** POINT 6 *** Handle the received result data carefully and securely,
        // even though the data comes from a partner application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        logLine(String.format("  %s records deleted", count));
    }

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
