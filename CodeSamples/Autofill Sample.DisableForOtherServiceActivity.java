package org.jssec.android.autofillframework.autofillapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.EditText;
import android.widget.TextView;

import org.jssec.android.autofillframework.R;

public class DisableForOtherServiceActivity extends AppCompatActivity {
    private boolean mIsAutofillEnabled = false;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable_for_other_service_activity);

        mUsernameEditText = (EditText)findViewById(R.id.field_username);
        mPasswordEditText = (EditText)findViewById(R.id.field_password);

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
        //Because the floating-toolbar is not supported for this Activity, 
        // Autofill may be used by selecting "Automatic Input"
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAutofillStatus();

        if (!mIsAutofillEnabled) {
            View rootView = this.getWindow().getDecorView();
            //If not using Autofill service within the same package, make all Views ineligible for Autofill
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        } else {
            //If using Autofill service within the same package, make all Views eligible for Autofill
            //View#setImportantForAutofill() may also be called for specific Views
            View rootView = this.getWindow().getDecorView();
            rootView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_AUTO);
        }
    }
    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //Validate data obtained from View
        if (!Util.validateUsername(username) || !Util.validatePassword(password)) {
            //appropriate error handling
        }

        //Send username, password to server

        finish();
    }

    private void resetFields() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }

    private void updateAutofillStatus() {
        AutofillManager mgr = getSystemService(AutofillManager.class);

        mIsAutofillEnabled = mgr.hasEnabledAutofillServices();

        TextView statusView = (TextView) findViewById(R.id.label_autofill_status);
        String status = "Our autofill service is --.";
        if (mIsAutofillEnabled) {
            status = "autofill service within same package is enabled";
        } else {
            status = "autofill service within same package is disabled";
        }
        statusView.setText(status);
    }
}
