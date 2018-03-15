package org.jssec.android.accountmanager.user;

import java.io.IOException;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.Utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends Activity {

    // Information of the Authenticator to be used
    private static final String JSSEC_ACCOUNT_TYPE = "org.jssec.android.accountmanager";
    private static final String JSSEC_TOKEN_TYPE = "webservice";
    private TextView mLogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        
        mLogView = (TextView)findViewById(R.id.logview);
    }

    public void addAccount(View view) {
        logLine();
        logLine("Add a new account");

        // *** POINT 1 *** Execute the account process after verifying if the authenticator is regular one.
        if (!checkAuthenticator()) return;

        AccountManager am = AccountManager.get(this);
        am.addAccount(JSSEC_ACCOUNT_TYPE, JSSEC_TOKEN_TYPE, null, null, this,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle result = future.getResult();
                            String type = result.getString(AccountManager.KEY_ACCOUNT_TYPE);
                            String name = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                            if (type != null && name != null) {
                                logLine("Add the following accounts:");
                                logLine("  Account type: %s", type);
                                logLine("  Account name: %s", name);
                            } else {
                                String code = result.getString(AccountManager.KEY_ERROR_CODE);
                                String msg = result.getString(AccountManager.KEY_ERROR_MESSAGE);
                                logLine("The account cannot be added");
                                logLine("  Error code %s: %s", code, msg);
                            }
                        } catch (OperationCanceledException e) {
                        } catch (AuthenticatorException e) {
                        } catch (IOException e) {
                        }
                    }
                },
                null);
    }

    public void getAuthToken(View view) {
        logLine();
        logLine("Get token");

        // *** POINT 1 *** After checking that the Authenticator is the regular one, execute account process.
        if (!checkAuthenticator()) return;

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(JSSEC_ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            am.getAuthToken(account, JSSEC_TOKEN_TYPE, null, this,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Bundle result = future.getResult();
                                String name = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                                String authtoken = result.getString(AccountManager.KEY_AUTHTOKEN);
                                logLine("%s-san's token:", name);
                                if (authtoken != null) {
                                    logLine("    %s", authtoken);
                                } else {
                                    logLine("    Couldn't get");
                                }
                            } catch (OperationCanceledException e) {
                                logLine("  Exception: %s",e.getClass().getName());
                            } catch (AuthenticatorException e) {
                                logLine("  Exception: %s",e.getClass().getName());
                            } catch (IOException e) {
                                logLine("  Exception: %s",e.getClass().getName());
                            }
                        }
                    }, null);
        } else {
            logLine("Account is not registered.");
        }
    }

    // *** POINT 1 *** Verify that Authenticator is regular one.
    private boolean checkAuthenticator() {
        AccountManager am = AccountManager.get(this);
        String pkgname = null;
        for (AuthenticatorDescription ad : am.getAuthenticatorTypes()) {
            if (JSSEC_ACCOUNT_TYPE.equals(ad.type)) {
                pkgname = ad.packageName;
                break;
            }
        }
        
        if (pkgname == null) {
            logLine("Authenticator cannot be found.");
            return false;
        }
        
        logLine("  Account type: %s", JSSEC_ACCOUNT_TYPE);
        logLine("  Package name of Authenticator: ");
        logLine("    %s", pkgname);

        if (!PkgCert.test(this, pkgname, getTrustedCertificateHash(this))) {
            logLine("  It's not regular Authenticator(certificate is not matched.)");
            return false;
        }
        
        logLine("  This is regular Authenticator.");
        return true;
    }

    // Certificate hash value of regular Authenticator application 
    // Certificate hash value can be checked in sample applciation JSSEC CertHash Checker
    private String getTrustedCertificateHash(Context context) {
        if (Utils.isDebuggable(context)) {
            // Certificate hash value of  debug.keystore "androiddebugkey"
            return "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
        } else {
            // Certificate hash value of  keystore "my company key"
            return "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
        }
    }
    
    private void log(String str) {
        mLogView.append(str);
    }
    
    private void logLine(String line) {
        log(line + "\n");
    }
    
    private void logLine(String fmt, Object... args) {
        logLine(String.format(fmt, args));
    }

    private void logLine() {
        log("\n");
    }
}
