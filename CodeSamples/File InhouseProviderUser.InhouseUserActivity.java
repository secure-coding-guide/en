package org.jssec.android.file.inhouseprovideruser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.SigPerm;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;

public class InhouseUserActivity extends Activity {

    // Content Provider information of destination (requested provider)
    private static final String AUTHORITY = "org.jssec.android.file.inhouseprovider";

    // In-house signature permission
    private static final String MY_PERMISSION = "org.jssec.android.file.inhouseprovider.MY_PERMISSION";

    // In-house certificate hash value
    private static String sMyCertHash = null;

    private static String myCertHash(Context context) {
        if (sMyCertHash == null) {
            if (Utils.isDebuggable(context)) {
                // Certificate hash value of  debug.keystore "androiddebugkey"
                sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
            } else {
                // Certificate hash value of  keystore "my company key"
                sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
            }
        }
        return sMyCertHash;
    }

    // Get package name of destination (requested) content provider.
    private static String providerPkgname(Context context, String authority) {
        String pkgname = null;
        PackageManager pm = context.getPackageManager();
        ProviderInfo pi = pm.resolveContentProvider(authority, 0);
        if (pi != null)
            pkgname = pi.packageName;
        return pkgname;
    }

    public void onReadFileClick(View view) {

        logLine("[ReadFile]");

        // Verify that in-house-defined signature permission is defined by in-house application.
        if (!SigPerm.test(this, MY_PERMISSION, myCertHash(this))) {
            logLine("  In-house-defined signature permission is not defined by in-house application.");
            return;
        }

        // Verify that the certificate of destination (requested) content provider application is in-house certificate.
        String pkgname = providerPkgname(this, AUTHORITY);
        if (!PkgCert.test(this, pkgname, myCertHash(this))) {
            logLine("  Destination (Requested) Content Provider is not in-house application.");
            return;
        }

        // Only the information which can be disclosed to in-house only content provider application, can be included in a request.
        ParcelFileDescriptor pfd = null;
        try {
            pfd = getContentResolver().openFileDescriptor(
                    Uri.parse("content://" + AUTHORITY), "r");
        } catch (FileNotFoundException e) {
            android.util.Log.e("InhouseUserActivity", "no file");
        }

        if (pfd != null) {
            FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());

            if (fis != null) {
                try {
                    byte[] buf = new byte[(int) fis.getChannel().size()];
                    fis.read(buf);
                    // *** POINT 2 *** Handle received result data carefully and securely,
                    // even though the data came from in-house applications.
                    // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
                    logLine(new String(buf));
                } catch (IOException e) {
                    android.util.Log.e("InhouseUserActivity", "failed to read file");
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        android.util.Log.e("ExternalFileActivity", "failed to close file");
                    }
                }
            }
            try {
                pfd.close();
            } catch (IOException e) {
                android.util.Log.e("ExternalFileActivity", "failed to close file descriptor");
            }

        } else {
            logLine("  null file descriptor");
        }
    }

    private TextView mLogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView) findViewById(R.id.logview);
    }

    private void logLine(String line) {
        mLogView.append(line);
        mLogView.append("\n");
    }
}
