package org.jssec.android.signasymmetrickey;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class RsaSignAsymmetricKey {

    // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
    // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
    // Parameters passed to the getInstance method of the Cipher class: Encryption algorithm, block encryption mode, padding rule 
    // In this sample, we choose the following parameter values: encryption algorithm=RSA, block encryption mode=NONE, padding rule=OAEPPADDING.
    private static final String TRANSFORMATION = "SHA256withRSA";
    
    // encryption algorithm
    private static final String KEY_ALGORITHM = "RSA";
    
    // *** POINT 3 *** Use a key of length sufficient to guarantee the signature strength.
    // Check the length of the key
    private static final int MIN_KEY_LENGTH = 2000;

    RsaSignAsymmetricKey() {
    }
    
    public final byte[] sign(final byte[] plain, final byte[] keyData) {
        // In general, signature procedures should be implemented on the server side;
        // however, in this sample code we have implemented signature processing within the application to ensure confirmation of proper execution.
        // When using this sample code in real-world applications, be careful not to retain any private keys within the application. 

        byte[] sign = null;

        try {
            // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
            // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            Signature signature = Signature.getInstance(TRANSFORMATION);

            PrivateKey privateKey = generatePriKey(keyData);
            signature.initSign(privateKey);
            signature.update(plain);

            sign = signature.sign();
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (SignatureException e) {
        } finally {
        }

        return sign;
    }

    public final boolean verify(final byte[] sign, final byte[] plain, final byte[] keyData) {

        boolean ret = false;
        
        try {
            // *** POINT 1 *** Explicitly specify the encryption mode and the padding.
            // *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
            Signature signature = Signature.getInstance(TRANSFORMATION);

            PublicKey publicKey = generatePubKey(keyData);
            signature.initVerify(publicKey);                
            signature.update(plain);
            
            ret = signature.verify(sign);
                        
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (SignatureException e) {
        } finally {
        }

        return ret;
    }

    private static final PublicKey generatePubKey(final byte[] keyData) {
        PublicKey publicKey = null;
        KeyFactory keyFactory = null;
        
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyData));         
        } catch (IllegalArgumentException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
        }

        // *** POINT 3 *** Use a key of length sufficient to guarantee the signature strength.
        // Check the length of the key
        if (publicKey instanceof RSAPublicKey) {
            int len = ((RSAPublicKey) publicKey).getModulus().bitLength();
            if (len < MIN_KEY_LENGTH) {
                publicKey = null;
            }
        }

        return publicKey;
    }

    private static final PrivateKey generatePriKey(final byte[] keyData) {
        PrivateKey privateKey = null;
        KeyFactory keyFactory = null;
        
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyData));
        } catch (IllegalArgumentException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } finally {
        }

        return privateKey;
    }
}
