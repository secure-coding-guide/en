package authentication.fingerprint.android.jssec.org.fingerprintauthentication;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public class FingerprintAuthentication {
    private static final String KEY_NAME = "KeyForFingerprintAuthentication";
    private static final String PROVIDER_NAME = "AndroidKeyStore";

    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;
    private CancellationSignal mCancellationSignal;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher mCipher;

    public FingerprintAuthentication(Context context) {
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        reset();
    }

    public boolean startAuthentication(final FingerprintManager.AuthenticationCallback callback) {
        if (!generateAndStoreKey())
            return false;

        if (!initializeCipherObject())
            return false;

        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mCipher);

        mCancellationSignal = new CancellationSignal();

        // Callback to receive the results of fingerprint authentication
        FingerprintManager.AuthenticationCallback hook = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                if (callback != null) callback.onAuthenticationError(errorCode, errString);
                reset();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                if (callback != null) callback.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                if (callback != null) callback.onAuthenticationSucceeded(result);
                reset();
            }

            @Override
            public void onAuthenticationFailed() {
                if (callback != null) callback.onAuthenticationFailed();
            }
        };

        // Execute fingerprint authentication
        mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, hook, null);

        return true;
    }

    public boolean isAuthenticating() {
        return mCancellationSignal != null && !mCancellationSignal.isCanceled();
    }

    public void cancel() {
        if (mCancellationSignal != null) {
            if (!mCancellationSignal.isCanceled())
                mCancellationSignal.cancel();
        }
    }

    private void reset() {
        try {
            // *** POINT 2 ***  Obtain an instance from the "AndroidKeyStore" Provider
            mKeyStore = KeyStore.getInstance(PROVIDER_NAME);
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER_NAME);
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                    + "/" + KeyProperties.BLOCK_MODE_CBC
                    + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (KeyStoreException | NoSuchPaddingException
                | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("failed to get cipher instances", e);
        }
        mCancellationSignal = null;
    }

    public boolean isFingerprintAuthAvailable() {
        return (mKeyguardManager.isKeyguardSecure()
                && mFingerprintManager.hasEnrolledFingerprints()) ? true : false;
    }

    public boolean isFingerprintHardwareDetected() {
        return mFingerprintManager.isHardwareDetected();
    }

    private boolean generateAndStoreKey() {
        try {
            mKeyStore.load(null);
            if (mKeyStore.containsAlias(KEY_NAME))
                mKeyStore.deleteEntry(KEY_NAME);
            mKeyGenerator.init(
                    // *** POINT 4 *** When creating (registering) keys, use an encryption algorithm that is not vulnerable (meets standards)
                    new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            // *** POINT 5 *** When creating (registering) keys, enable requests for user (fingerprint) authentication (do not specify the duration over which authentication is enabled)
                            .setUserAuthenticationRequired(true)
                            .build());
            // Generate a key and store it in Keystore(AndroidKeyStore)
            mKeyGenerator.generateKey();
            return true;
        } catch (IllegalStateException e) {
            return false;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | KeyStoreException | IOException e) {
            throw new RuntimeException("failed to generate a key", e);
        }
    }

    private boolean initializeCipherObject() {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER_NAME);
            KeyInfo info = (KeyInfo) factory.getKeySpec(key, KeyInfo.class);

            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            // *** POINT 6 *** Design your app on the assumption that the status of fingerprint registration will change between when keys are created and when keys are used
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException | InvalidKeyException e) {
            throw new RuntimeException("failed to init Cipher", e);
        }
    }

}
