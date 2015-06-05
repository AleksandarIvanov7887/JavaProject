package com.track.be.utilities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

public class RZSecurity {
    
    private static final String SALT1 = "=Ю?яg0_c>na=_Xdg!rwa/bbXH6Fo| FЧ-@Uю*E%3=Ю?яg0_c>na=_Xdg!rwa/bbXJkf^^&N>AK$j+]JE^h+)л6C=^";
    private static final SecureRandom random = new SecureRandom();

    private static byte[] hashRipeMD160(byte[] data) {
        RIPEMD160Digest d = new RIPEMD160Digest();
        d.update(data, 0, data.length);
        byte[] o = new byte[d.getDigestSize()];
        d.doFinal(o, 0);
        return Hex.encode(o);
    }

    private static byte[] hashSHA256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(data);

            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RZSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getSALT2() {
        return new BigInteger(256, random).toString(64);
    }

    private static byte[] concatByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }

    public static String encryptData(String data, String salt2) {
        try {
            //hashedpass = HASHCODE1 ( HASHCODE2(‘SALT1password’) + ‘SALT2′)

            String complexData = SALT1.concat(data);
            byte[] byteData = complexData.getBytes("UTF-8");
            byte[] firstEncryptedLevel = concatByteArrays(hashSHA256(byteData), salt2.getBytes("UTF-8"));
            byte[] encryptedData = hashSHA256(firstEncryptedLevel);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < encryptedData.length; i++) {
                hexString.append(Integer.toHexString(0xFF & encryptedData[i]));
            }

            return hexString.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RZSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static String generateLoginHash() {
        return RZSecurity.getSALT2();
    }

    public static String generateBase64Hash(String object) {
        return new String(Base64.encode(object.getBytes()));
    }

    public static String decodeBase64Hash(String data) {
        return new String(Base64.decode(data));
    }
    
    public static String getRandomGeneratedString() {
        return new BigInteger(130, random).toString(32);
    }

    private RZSecurity() {}
}
