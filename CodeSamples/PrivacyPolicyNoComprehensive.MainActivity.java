package org.jssec.android.privacypolicynocomprehensive;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final String BASE_URL = "https://www.example.com/pp";
    private static final String GET_ID_URI = BASE_URL + "/get_id.php";
    private static final String SEND_DATA_URI = BASE_URL + "/send_data.php";
    private static final String DEL_ID_URI = BASE_URL + "/del_id.php";

    private static final String ID_KEY = "id";
    private static final String NICK_NAME_KEY = "nickname";

    private static final String PRIVACY_POLICY_PREF_NAME = "privacypolicy_preference";

    private String UserId = "";

    private TextWatcher watchHandler = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean buttonEnable = (s.length() > 0);

            MainActivity.this.findViewById(R.id.buttonStart).setEnabled(buttonEnable);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch user ID from serverFetch user ID from server
        new GetDataAsyncTask().execute();

        findViewById(R.id.buttonStart).setEnabled(false);
        ((TextView) findViewById(R.id.editTextNickname)).addTextChangedListener(watchHandler);
    }

    public void onSendToServer(View view) {
        String nickname = ((TextView) findViewById(R.id.editTextNickname)).getText().toString();
        Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + "\n - nickname : " + nickname, Toast.LENGTH_SHORT).show();
        new sendDataAsyncTack().execute(SEND_DATA_URI, UserId, nickname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_show_pp:
            // *** POINT 1 *** Provide methods by which the user can review the application privacy policy.
            Intent intent = new Intent();
            intent.setClass(this, WebViewAssetsActivity.class);
            startActivity(intent);
            return true;
        case R.id.action_del_id:
            // *** POINT 2 *** Provide methods by which transmitted data can be deleted by user operations.
            new sendDataAsyncTack().execute(DEL_ID_URI, UserId);
            return true;
        case R.id.action_donot_send_id:
            // *** POINT 3 *** Provide methods by which transmitting data can be stopped by user operations.

            // In this sample application if the user data cannot be sent by user operations,
            // finish the application because we do nothing.
            String message  = getString(R.string.stopSendUserData);
            Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + message, Toast.LENGTH_SHORT).show();
            finish();
            
            return true;
        }
        return false;
    }

    private class GetDataAsyncTask extends AsyncTask<String, Void, String> {
        private String extMessage = "";

        @Override
        protected String doInBackground(String... params) {
            // *** POINT 4 *** Use UUIDs or cookies to keep track of user data
            // In this sample we use an ID generated on the server side
            SharedPreferences sp = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
            UserId = sp.getString(ID_KEY, null);
            if (UserId == null) {
                // No token in SharedPreferences; fetch ID from server
                try {
                    UserId = NetworkUtil.getCookie(GET_ID_URI, "", "id");
                } catch (IOException e) {
                    // Catch exceptions such as certification errors
                    extMessage = e.toString();
                }

                // Store the fetched ID in SharedPreferences
                sp.edit().putString(ID_KEY, UserId).commit();
            }
            return UserId;
        }

        @Override
        protected void onPostExecute(final String data) {
            String status = (data != null) ? "success" : "error";
            Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + status + " : " + extMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private class sendDataAsyncTack extends AsyncTask<String, Void, Boolean> {
        private String extMessage = "";

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            String id = params[1];
            String nickname = params.length > 2 ? params[2] : null;

            Boolean result = false;
            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put(ID_KEY, id);

                if (nickname != null)
                    jsonData.put(NICK_NAME_KEY, nickname);

                NetworkUtil.sendJSON(url, "", jsonData.toString());

                result = true;
            } catch (IOException e) {
                // Catch exceptions such as certification errors
                extMessage = e.toString();
            } catch (JSONException e) {
                extMessage = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String status = result ? "Success" : "Error";
            Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + status + " : " + extMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
