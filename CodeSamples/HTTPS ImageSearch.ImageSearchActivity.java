package org.jssec.android.https.imagesearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSearchActivity extends Activity {

    private EditText mQueryBox;
    private TextView mMsgBox;
    private ImageView mImgBox;
    private AsyncTask<String, Void, Object> mAsyncTask ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mQueryBox = (EditText)findViewById(R.id.querybox);
        mMsgBox = (TextView)findViewById(R.id.msgbox);
        mImgBox = (ImageView)findViewById(R.id.imageview);
    }

    @Override
    protected void onPause() {
        // After this, Activity may be deleted, so cancel the asynchronization process in advance.
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        super.onPause();
    }

    public void onHttpSearchClick(View view) {
        String query = mQueryBox.getText().toString();
        mMsgBox.setText("HTTP:" + query);
        mImgBox.setImageBitmap(null);
        
        // Cancel, since the last asynchronous process might not have been finished yet.
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // Since cannot communicate by UI thread, communicate by worker thread by AsynchTask.
        mAsyncTask = new HttpImageSearch() {
            @Override
            protected void onPostExecute(Object result) {
                // Process the communication result by UI thread.
                if (result == null) {
                    mMsgBox.append("\nException occurs\n");
                } else if (result instanceof Exception) {
                    Exception e = (Exception)result;
                    mMsgBox.append("\nException occurs\n" + e.toString());
                } else {
                    // Exception process when image display is omitted here, since it's sample.
                    byte[] data = (byte[])result;
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImgBox.setImageBitmap(bmp);
                }
            }
                }.execute(query);   // pass search character string and start asynchronous process
    }

    public void onHttpsSearchClick(View view) {
        String query = mQueryBox.getText().toString();
        mMsgBox.setText("HTTPS:" + query);
        mImgBox.setImageBitmap(null);
        
        // Cancel, since the last asynchronous process might not have been finished yet.
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        
        // Since cannot communicate by UI thread, communicate by worker thread by AsynchTask.
        mAsyncTask = new HttpsImageSearch() {
            @Override
            protected void onPostExecute(Object result) {
                // Process the communication result by UI thread.
                if (result instanceof Exception) {
                    Exception e = (Exception)result;
                    mMsgBox.append("\nException occurs\n" + e.toString());
                } else {
                    byte[] data = (byte[])result;
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImgBox.setImageBitmap(bmp);
                }
            }
       }.execute(query);    // pass search character string and start asynchronous process
    }
}
