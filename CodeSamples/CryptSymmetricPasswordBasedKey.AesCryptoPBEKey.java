package org.jssec.android.cryptsymmetricpasswordbasedkey;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

public final class AesCryptoPBEKey {

    // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
    // *** POINT 2 *** Use strong encryption technologies (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
    // Parameters passed to the getInstance method of the Cipher class: Encryption algorithm, block encryption mode, padding rule 
    // In this sample, we choose the following parameter values: encryption algorithm=AES, block encryption mode=CBC, padding rule=PKCS7Padding
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";

    //  A string used to fetch an instance of the class that generates the key
    private static final String KEY_GENERATOR_MODE = "PBEWITHSHA256AND128BITAES-CBC-BC";

    // *** POINT 3 *** When generating a key from a password, use Salt.
    // Salt length in bytes 
    public static final int SALT_LENGTH_BYTES = 20;

    // *** POINT 4 *** When generating a key from a password, specify an appropriate hash iteration count.
    // Set the number of mixing repetitions used when generating keys via PBE
    private static final int KEY_GEN_ITERATION_COUNT = 1024;

    // *** POINT 5 *** Use a key of length sufficient to guarantee the strength of encryption.
    // Key length in bits
    private static final int KEY_LENGTH_BITS = 128;

    private byte[] mIV = null;
    private byte[] mSalt = null;

    public byte[] getIV() {
        return mIV;
    }

    public byte[] getSalt() {
        return mSalt;
    }

    AesCryptoPBEKey(final byte[] iv, final byte[] salt) {
        mIV = iv;
        mSalt = salt;
    }

    AesCryptoPBEKey() {
        mIV = null;
        initSalt();
    }

    private void initSalt() {
        mSalt = new byte[SALT_LENGTH_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(mSalt);
    }

    public final byte[] encrypt(final byte[] plain, final char[] password) {
        byte[] encrypted = null;

        try {
            // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
            // *** POINT 2 *** Use strong encryption technologies (specifically, technologies that meet the relevant criteria), including algorithms, modes, and padding.
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // *** POINT 3 *** When generating keys from passwords, use Salt.
            SecretKey secretKey = generateKey(password, mSalt);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            mIV = cipher.getIV();

            encrypted = cipher.doFinal(plain);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return encrypted;
    }

    public final byte[] decrypt(final byte[] encrypted, final char[] password) {
        byte[] plain = null;

        try {
            // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
            // *** POINT 2 *** Use strong encryption technologies (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // *** POINT 3 *** When generating a key from a password, use Salt.
            SecretKey secretKey = generateKey(password, mSalt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(mIV);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            plain = cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (BadPaddingException e) {
        } finally {
        }

        return plain;
    }

    private static final SecretKey generateKey(final char[] password, final byte[] salt) {
        SecretKey secretKey = null;
        PBEKeySpec keySpec = null;

        try {
            // *** POINT 2 *** Use strong encryption technologies (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            // Fetch an instance of the class that generates the key
            // In this example, we use a KeyFactory that uses SHA256 to generate AES-CBC 128-bit keys.
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATOR_MODE);

            // *** POINT 3 *** When generating a key from a password, use Salt.
            // *** POINT 4 *** When generating a key from a password, specify an appropriate hash iteration count.
            // *** POINT 5 *** Use a key of length sufficient to guarantee the strength of encryption.
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
