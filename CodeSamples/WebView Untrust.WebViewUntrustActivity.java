package org.jssec.webview.untrust;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;

public class WebViewUntrustActivity extends Activity {
    /*
     * Show contents which are NOT managed in-house (Sample program works as a simple browser)
     */

    private EditText textUrl;
    private Button buttonGo;
    private WebView webView;

    // Activity definition to handle any URL request 
    private class WebViewUnlimitedClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            textUrl.setText(url);
            return true;
        }

        // Start reading Web page 
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            buttonGo.setEnabled(false);
            textUrl.setText(url);
        }

        // Show SSL error dialog
        // And abort connection. 
        @Override
        public void onReceivedSslError(WebView webview,
                SslErrorHandler handler, SslError error) {
            
            // *** POINT 1 *** Handle SSL error from WebView appropriately
            AlertDialog errorDialog = createSslErrorDialog(error);
            errorDialog.show();
            handler.cancel();
            textUrl.setText(webview.getUrl());
            buttonGo.setEnabled(true);
        }

        // After loading Web page, show the URL in EditText.
        @Override
        public void onPageFinished(WebView webview, String url) {
            textUrl.setText(url);
            buttonGo.setEnabled(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewUnlimitedClient());

        // *** POINT 2 *** Disable JavaScript of WebView
        // Explicitly disable JavaScript even though it is disabled by default.
        webView.getSettings().setJavaScriptEnabled(false);

        webView.loadUrl(getString(R.string.texturl));
        textUrl = (EditText) findViewById(R.id.texturl);
        buttonGo = (Button) findViewById(R.id.go);
    }

    public void onClickButtonGo(View v) {
        webView.loadUrl(textUrl.getText().toString());
    }

    private AlertDialog createSslErrorDialog(SslError error) {
        // Error message to show in this dialog
        String errorMsg = createErrorMessage(error);
        // Handler for OK button
        DialogInterface.OnClickListener onClickOk = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
            }
        };
        // Create a dialog
        AlertDialog dialog = new AlertDialog.Builder(
                WebViewUntrustActivity.this).setTitle("SSL connection error")
                .setMessage(errorMsg).setPositiveButton("OK", onClickOk)
                .create();
        return dialog;
    }

    private String createErrorMessage(SslError error) {
        SslCertificate cert = error.getCertificate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        StringBuilder result = new StringBuilder()
        .append("The site's certification is NOT valid. Connection was disconnected.\n\nError:\n");
        switch (error.getPrimaryError()) {
        case SslError.SSL_EXPIRED:
            result.append("The certificate is no longer valid.\n\nThe expiration date is ")
            .append(dateFormat.format(cert.getValidNotAfterDate()));
            return result.toString();
        case SslError.SSL_IDMISMATCH:
            result.append("Host name doesn't match. \n\nCN=")
            .append(cert.getIssuedTo().getCName());
            return result.toString();
        case SslError.SSL_NOTYETVALID:
            result.append("The certificate isn't valid yet.\n\nIt will be valid from ")
            .append(dateFormat.format(cert.getValidNotBeforeDate()));
            return result.toString();
        case SslError.SSL_UNTRUSTED:
            result.append("Certificate Authority which issued the certificate is not reliable.\n\nCertificate Authority\n")
            .append(cert.getIssuedBy().getDName());
            return result.toString();
        default:
            result.append("Unknown error occured. ");
            return result.toString();
        }
    }
}
