package authentication.fingerprint.android.jssec.org.fingerprintauthentication;

import android.app.AlertDialog;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class MainActivity extends AppCompatActivity {

    private FingerprintAuthentication mFingerprintAuthentication;
    private static final String SENSITIVE_DATA = "sensitive data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFingerprintAuthentication = new FingerprintAuthentication(this);

        Button button_fingerprint_auth = (Button) findViewById(R.id.button_fingerprint_auth);
        button_fingerprint_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFingerprintAuthentication.isAuthenticating()) {
                    if (authenticateByFingerprint()) {
                        showEncryptedData(null);
                        setAuthenticationState(true);
                    }
                } else {
                    mFingerprintAuthentication.cancel();
                }
            }
        });
    }

    private boolean authenticateByFingerprint() {

        if (!mFingerprintAuthentication.isFingerprintHardwareDetected()) {
            // Terminal is not equipped with a fingerprint sensor
            return false;
        }

        if (!mFingerprintAuthentication.isFingerprintAuthAvailable()) {
            // *** POINT 3 *** Notify users that fingerprint registration will be required to create a key
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("No fingerprint information has been registered.\n" +
                            "Click \"Security\" on the Settings menu to register fingerprints. \n" +
                            "Registering fingerprints allows easy authentication.")
                    .setPositiveButton("OK", null)
                    .show();
            return false;
        }

        // Callback that receives the results of fingerprint authentication
        FingerprintManager.AuthenticationCallback callback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                showMessage(errString, R.color.colorError);
                reset();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                showMessage(helpString, R.color.colorHelp);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

                Cipher cipher = result.getCryptoObject().getCipher();
                try {
                    // *** POINT 7*** Restrict encrypted data to items that can be restored (replaced) by methods other than fingerprint authentication
                    byte[] encrypted = cipher.doFinal(SENSITIVE_DATA.getBytes());
                    showEncryptedData(encrypted);
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                }

                showMessage(getString(R.string.fingerprint_auth_succeeded), R.color.colorAuthenticated);
                reset();
            }

            @Override
            public void onAuthenticationFailed() {
                showMessage(getString(R.string.fingerprint_auth_failed), R.color.colorError);
            }
        };

        if (mFingerprintAuthentication.startAuthentication(callback)) {
            showMessage(getString(R.string.fingerprint_processing), R.color.colorNormal);
            return true;
        }

        return false;
    }


    private void setAuthenticationState(boolean authenticating) {
        Button button = (Button) findViewById(R.id.button_fingerprint_auth);
        button.setText(authenticating ? R.string.cancel : R.string.authenticate);
    }

    private void showEncryptedData(byte[] encrypted) {
        TextView textView = (TextView) findViewById(R.id.encryptedData);
        if (encrypted != null) {
            textView.setText(Base64.encodeToString(encrypted, 0));
        } else {
            textView.setText("");
        }
    }

    private String getCurrentTimeString() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        return simpleDateFormat.format(date);
    }

    private void showMessage(CharSequence msg, int colorId) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getCurrentTimeString() + " :\n" + msg);
        textView.setTextColor(getResources().getColor(colorId, null));
    }

    private void reset() {
        setAuthenticationState(false);
    }
}
