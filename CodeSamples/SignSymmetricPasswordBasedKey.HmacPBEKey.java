package org.jssec.android.signsymmetricpasswordbasedkey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class HmacPBEKey {

    // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
    // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
    // Parameters passed to the getInstance method of the Mac class: Authentication mode
    private static final String TRANSFORMATION = "PBEWITHHMACSHA1";

    // A string used to fetch an instance of the class that generates the key
    private static final String KEY_GENERATOR_MODE = "PBEWITHHMACSHA1";

    // *** POINT 3 *** When generating a key from a password, use Salt.
    // Salt length in bytes 
    public static final int SALT_LENGTH_BYTES = 20;

    // *** POINT 4 *** When generating a key from a password, specify an appropriate hash iteration count.
    // Set the number of mixing repetitions used when generating keys via PBE
    private static final int KEY_GEN_ITERATION_COUNT = 1024;

    // *** POINT 5 *** Use a key of length sufficient to guarantee the MAC strength.
    // Key length in bits
    private static final int KEY_LENGTH_BITS = 160;

    private byte[] mSalt = null;

    public byte[] getSalt() {
        return mSalt;
    }

    HmacPBEKey() {
        initSalt();
    }

    HmacPBEKey(final byte[] salt) {
        mSalt = salt;
    }

    private void initSalt() {
        mSalt = new byte[SALT_LENGTH_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(mSalt);
    }

    public final byte[] sign(final byte[] plain, final char[] password) {
        return calculate(plain, password);
    }

    private final byte[] calculate(final byte[] plain, final char[] password) {
        byte[] hmac = null;

        try {
            // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
            // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            Mac mac = Mac.getInstance(TRANSFORMATION);

            // *** POINT 3 *** When generating a key from a password, use Salt.
            SecretKey secretKey = generateKey(password, mSalt);
            mac.init(secretKey);

            hmac = mac.doFinal(plain);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } finally {
        }

        return hmac;
    }

    public final boolean verify(final byte[] hmac, final byte[] plain, final char[] password) {

        byte[] hmacForPlain = calculate(plain, password);

        if (Arrays.equals(hmac, hmacForPlain)) {
            return true;
        }
        return false;
    }

    private static final SecretKey generateKey(final char[] password, final byte[] salt) {
        SecretKey secretKey = null;
        PBEKeySpec keySpec = null;

        try {
            // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            // Fetch an instance of the class that generates the key 
            // In this example, we use a KeyFactory that uses SHA1 to generate AES-CBC 128-bit keys.
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATOR_MODE);

            // *** POINT 3 *** When generating a key from a password, use Salt.
            // *** POINT 4 *** When generating a key from a password, specify an appropriate hash iteration count.
            // *** POINT 5 *** Use a key of length sufficient to guarantee the MAC strength.
            keySpec = new PBEKeySpec(password, salt, KEY_GEN_ITERATION_COUNT, KEY_LENGTH_BITS);
            // Clear password
            Arrays.fill(password, '?');
            // Generate the key
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
            keySpec.clearPassword();
        }

        return secretKey;
    }

}
