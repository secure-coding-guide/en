package org.jssec.android.https.privatecertificate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivateCertificateHttpsActivity extends Activity {

    private EditText mUrlBox;
    private TextView mMsgBox;
    private ImageView mImgBox;
    private AsyncTask<String, Void, Object> mAsyncTask ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlBox = (EditText)findViewById(R.id.urlbox);
        mMsgBox = (TextView)findViewById(R.id.msgbox);
        mImgBox = (ImageView)findViewById(R.id.imageview);
    }
    
    @Override
    protected void onPause() {
        // After this, Activity may be discarded, so cancel asynchronous process in advance.
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        super.onPause();
    }
    
    public void onClick(View view) {
        String url = mUrlBox.getText().toString();
        mMsgBox.setText(url);
        mImgBox.setImageBitmap(null);
        
        // Cancel, since the last asynchronous process might have not been finished yet.
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // Since cannot communicate through UI thread, communicate by worker thread by AsynchTask.
        mAsyncTask = new PrivateCertificateHttpsGet(this) {
            @Override
            protected void onPostExecute(Object result) {
                // Process the communication result through UI thread.
                if (result instanceof Exception) {
                    Exception e = (Exception)result;
                    mMsgBox.append("\nException occurs\n" + e.toString());
                } else {
                    byte[] data = (byte[])result;
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImgBox.setImageBitmap(bmp);
                }
            }
        }.execute(url); // Pass URL and start asynchronization process
    }
}
