package org.jssec.android.privacypolicy;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssec.android.privacypolicy.ConfirmFragment.DialogListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, DialogListener {
    private static final String BASE_URL = "https://www.example.com/pp";
    private static final String GET_ID_URI = BASE_URL + "/get_id.php";
    private static final String SEND_DATA_URI = BASE_URL + "/send_data.php";
    private static final String DEL_ID_URI = BASE_URL + "/del_id.php";

    private static final String ID_KEY = "id";
    private static final String LOCATION_KEY = "location";
    private static final String NICK_NAME_KEY = "nickname";

    private static final String PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY = "privacyPolicyComprehensiveAgreed";
    private static final String PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY = "privacyPolicyDiscreteType1Agreed";

    private static final String PRIVACY_POLICY_PREF_NAME = "privacypolicy_preference";
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 257;

    private String UserId = "";
    private LocationClient mLocationClient = null;

    private final int DIALOG_TYPE_COMPREHENSIVE_AGREEMENT = 1;
    private final int DIALOG_TYPE_PRE_CONFIRMATION = 2;

    private static final int VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW = 1;

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

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mLocationClient = new LocationClient(this, this, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
        int privacyPolicyAgreed = pref.getInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, -1);

        if (privacyPolicyAgreed <= VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW) {
            // *** POINT 1 *** On first launch (or application update), obtain broad consent to transmit user data that will be handled by the application.
            // When the application is updated, it is only necessary to renew the user's grant of broad consent if the updated application will handle new types of user data.
            ConfirmFragment dialog = ConfirmFragment.newInstance(R.string.privacyPolicy, R.string.agreePrivacyPolicy, DIALOG_TYPE_COMPREHENSIVE_AGREEMENT);
            dialog.setDialogListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialog.show(fragmentManager, "dialog");
        }

        // Used to obtain location data
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        super.onStop();
    }

    public void onSendToServer(View view) {
        // Check the status of user consent.
        // Actually, it is necessary to obtain consent for each user data type.
        SharedPreferences pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE);
        int privacyPolicyAgreed = pref.getInt(PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY, -1);
        if (privacyPolicyAgreed <= VERSION_TO_SHOW_COMPREHENSIVE_AGREEMENT_ANEW) {
            // *** POINT 3 *** Obtain specific consent before transmitting user data that requires particularly delicate handling.
            ConfirmFragment dialog = ConfirmFragment.newInstance(R.string.sendLocation, R.string.cofirmSendLocation, DIALOG_TYPE_PRE_CONFIRMATION);
            dialog.setDialogListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialog.show(fragmentManager, "dialog");
        } else {
            // Start transmission, since it has the user consent.
            onPositiveButtonClick(DIALOG_TYPE_PRE_CONFIRMATION);
        }
    }

    public void onPositiveButtonClick(int type) {
        if (type == DIALOG_TYPE_COMPREHENSIVE_AGREEMENT) {
            // *** POINT 1 *** On first launch (or application update), obtain broad consent to transmit user data that will be handled by the application.
            SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
            pref.putInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, getVersionCode());
            pref.apply();
        } else if (type == DIALOG_TYPE_PRE_CONFIRMATION) {
            // *** POINT 3 *** Obtain specific consent before transmitting user data that requires particularly delicate handling.
            if (mLocationClient != null && mLocationClient.isConnected()) {
                Location currentLocation = mLocationClient.getLastLocation();
                if (currentLocation != null) {
                    String locationData = "Latitude:" + currentLocation.getLatitude() + ", Longitude:" + currentLocation.getLongitude();
                    String nickname = ((TextView) findViewById(R.id.editTextNickname)).getText().toString();

                    Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + "\n - nickname : " + nickname + "\n - location : " + locationData, Toast.LENGTH_SHORT).show();

                    new SendDataAsyncTack().execute(SEND_DATA_URI, UserId, locationData, nickname);
                }
            }
            // Store the status of user consent.
            // Actually, it is necessary to obtain consent for each user data type.
            SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
            pref.putInt(PRIVACY_POLICY_DISCRETE_TYPE1_AGREED_KEY, getVersionCode());
            pref.apply();
        }
    }

    public void onNegativeButtonClick(int type) {
        if (type == DIALOG_TYPE_COMPREHENSIVE_AGREEMENT) {
            // *** POINT 2 *** If the user does not grant general consent, do not transmit user data.
            // In this sample application we terminate the application in this case.
            finish();
        } else if (type == DIALOG_TYPE_PRE_CONFIRMATION) {
            // *** POINT 4 *** If the user does not grant specific consent, do not transmit the corresponding data.
            // The user did not grant consent, so we do nothing.
        }
    }

    private int getVersionCode() {
        int versionCode = -1;
        PackageManager packageManager = this.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // This is sample, so omit the exception process
        }

        return versionCode;
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
                // *** POINT 5 *** Provide methods by which the user can review the application privacy policy.
                Intent intent = new Intent();
                intent.setClass(this, WebViewAssetsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_del_id:
                // *** POINT 6 *** Provide methods by which transmitted data can be deleted by user operations.
                new SendDataAsyncTack().execute(DEL_ID_URI, UserId);
                return true;
            case R.id.action_donot_send_id:
                // *** POINT 7 *** Provide methods by which transmitting data can be stopped by user operations.

                // If the user stop sending data, user consent is deemed to have been revoked.
                SharedPreferences.Editor pref = getSharedPreferences(PRIVACY_POLICY_PREF_NAME, MODE_PRIVATE).edit();
                pref.putInt(PRIVACY_POLICY_COMPREHENSIVE_AGREED_KEY, 0);
                pref.apply();

                // In this sample application if the user data cannot be sent by user operations,
                // finish the application because we do nothing.
                String message  = getString(R.string.stopSendUserData);
                Toast.makeText(MainActivity.this, this.getClass().getSimpleName() + " - " + message, Toast.LENGTH_SHORT).show();
                finish();

                return true;
        }

        return false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            Location currentLocation = mLocationClient.getLastLocation();
            if (currentLocation != null) {
                String locationData = "Latitude \t: " + currentLocation.getLatitude() + "\n\tLongitude \t: " + currentLocation.getLongitude();

                String text = "\n" + getString(R.string.your_location_title) + "\n\t" + locationData;

                TextView appText = (TextView) findViewById(R.id.appText);
                appText.setText(text);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisconnected() {
        mLocationClient = null;
    }

    private class GetDataAsyncTask extends AsyncTask<String, Void, String> {
        private String extMessage = "";

        @Override
        protected String doInBackground(String... params) {
            // *** POINT 8 *** Use UUIDs or cookies to keep track of user data
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

    private class SendDataAsyncTack extends AsyncTask<String, Void, Boolean> {
        private String extMessage = "";

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            String id = params[1];
            String location = params.length > 2 ? params[2] : null;
            String nickname = params.length > 3 ? params[3] : null;

            Boolean result = false;
            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put(ID_KEY, id);
                if (location != null)
                    jsonData.put(LOCATION_KEY, location);

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
