package org.jssec.android.sqlite;

import android.content.Intent;

public class SearchActivity extends SubActivity {
    protected void init(Intent intent) {
        this.setTitle(R.string.ACTIVITY_TITLE_SEARCH);            

        //Display a screen
        setContentView(R.layout.data_search);
    }
    protected boolean refrectEditText() {
        return true;
    }
}
