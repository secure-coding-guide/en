package org.jssec.android.sqlite;

import android.content.Intent;
import android.widget.TextView;

public class EditActivity extends SubActivity {
    private int     mRequestMode;
    
    protected void init(Intent intent) {
        mRequestMode = intent.getIntExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_NEW);

        if (mRequestMode == CommonData.REQUEST_NEW) {
            this.setTitle(R.string.ACTIVITY_TITLE_NEW);
        } else {
            this.setTitle(R.string.ACTIVITY_TITLE_EDIT);            
        }
        //Display a screen
        setContentView(R.layout.data_edit);     

        //Field No is not editable when edit mode.
        if (mRequestMode == CommonData.REQUEST_EDIT) {
            ((TextView)findViewById(R.id.Field_IdNo)).setFocusable(false);
            ((TextView)findViewById(R.id.Field_IdNo)).setClickable(false);
        }
    }
    protected boolean refrectEditText() {
        return true;
    }
}
