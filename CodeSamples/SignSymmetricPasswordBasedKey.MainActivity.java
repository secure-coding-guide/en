package org.jssec.android.signsymmetricpasswordbasedkey;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    static final int LAUNCH_SAVELOAD_ACTIVITY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onSave(View view) {
        launchIntent(true, getString(R.string.button_sign_save_title));
    }

    public void onLoad(View view) {
        launchIntent(false, getString(R.string.button_load_verify_title));
    }
    
    private void launchIntent(final boolean saveMode, final String buttonTitle) {
        Intent intent = new Intent(this, SaveLoadActivity.class);
        intent.putExtra("saveMode", saveMode);
        intent.putExtra("buttonTitle", buttonTitle);
        
        if (saveMode == true) {
            TextView textViewMemo = (TextView)findViewById(R.id.editTextMemo);
            String memo = textViewMemo.getText().toString();
            intent.putExtra("memo", memo);
        }
        
        startActivityForResult(intent, LAUNCH_SAVELOAD_ACTIVITY);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == LAUNCH_SAVELOAD_ACTIVITY) {
            TextView textViewMemo = (TextView)findViewById(R.id.editTextMemo);
            if (resultCode == SaveLoadActivity.RESULT_SUCCEEDED && intent != null) {
                String memo = intent.getStringExtra("memo");
                textViewMemo.setText(memo);
            } else if (resultCode == SaveLoadActivity.RESULT_FAILED){
                textViewMemo.setText(null);
                textViewMemo.setHint("Failed to verify your memo! Possibly corrupted!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
