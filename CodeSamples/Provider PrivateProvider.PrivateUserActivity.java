package org.jssec.android.provider.privateprovider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateUserActivity extends Activity {

    public void onQueryClick(View view) {

        logLine("[Query]");

        Cursor cursor = null;
        try {
            // *** POINT 4 *** Sensitive information can be sent since the destination provider is in the same application.
            cursor = getContentResolver().query(
                    PrivateProvider.Download.CONTENT_URI, null, null, null, null);

            // *** POINT 5 *** Handle received result data carefully and securely,
            // even though the data comes from the same application.
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

        // *** POINT 4 *** Sensitive information can be sent since the destination provider is in the same application.
        Uri uri = getContentResolver().insert(PrivateProvider.Download.CONTENT_URI, null);

        // *** POINT 5 *** Handle received result data carefully and securely,
        // even though the data comes from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        logLine("  uri:" + uri);
    }

    public void onUpdateClick(View view) {

        logLine("[Update]");

        // *** POINT 4 *** Sensitive information can be sent since the destination provider is in the same application.
        int count = getContentResolver().update(PrivateProvider.Download.CONTENT_URI, null, null, null);

        // *** POINT 5 *** Handle received result data carefully and securely,
        // even though the data comes from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        logLine(String.format("  %s records updated", count));
    }

    public void onDeleteClick(View view) {

        logLine("[Delete]");

        // *** POINT 4 *** Sensitive information can be sent since the destination provider is in the same application.
        int count = getContentResolver().delete(
                PrivateProvider.Download.CONTENT_URI, null, null);

        // *** POINT 5 *** Handle received result data carefully and securely,
        // even though the data comes from the same application.
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
