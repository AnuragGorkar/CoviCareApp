package com.example.covicareapp.logic;

import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecryptData {

    private static final String UNICODE_FORMAT = "UTF-8";

    private final static String HEX = "0123456789ABCDEF";
    private static final String TAG = "EncryptDecryptData";

    public EncryptDecryptData() {

    }

//    public static SecretKey getKeyFromPassword(String password, String salt)
//            throws NoSuchAlgorithmException, InvalidKeySpecException {
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 256, 256);
//        SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
//        return originalKey;
//    }

    public static SecretKey getKeyFromPassword64(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 64, 256);
        SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return originalKey;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    public static byte[] encryptString(String dataToEncrypt, SecretKey myKey, Cipher cipher) {
        try {
            Map<String, Object> mappedData = new HashMap<String, Object>();
            mappedData.put("Email", dataToEncrypt);

            byte[] text = mappedData.toString().getBytes(UNICODE_FORMAT);
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            byte[] textEncrypted = cipher.doFinal(text);

            return textEncrypted;
        } catch (Exception e) {
            return null;
        }
    }

    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public Map<String, Object> encryptEmail(String email) {
        try {
            String password = getAlphaNumericString(6);
            String salt = getAlphaNumericString(5);
            SecretKey key = getKeyFromPassword64(password, salt);

            Cipher cipher = Cipher.getInstance("AES");

            byte[] encryptedDataByteArray = encryptString(email, key, cipher);

            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("Password", password);
            returnMap.put("Salt", salt);
            returnMap.put("Data", toHex(encryptedDataByteArray));

            Log.i("Show Map", returnMap.toString());

            return returnMap;
        } catch (Exception e) {
            return new HashMap<String, Object>();
        }
    }

    public String decryptEmail(String mapString) {
        mapString = mapString.trim();
        mapString = mapString.substring(1, mapString.length() - 1);           //remove curly brackets

        String[] keyValuePairs = mapString.split(",");              //split the string to creat key-value pairs
        Map<String, Object> map = new HashMap<>();

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=", 2);
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword64((String) map.get("Password"), (String) map.get("Salt")));


            byte[] textDecrypted = cipher.doFinal(toByte(map.get("Data").toString()));
            String result = new String(textDecrypted);

            return result;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }

    public String decryptURLContinuous(String mapString) {
        mapString = mapString.trim();
        mapString = mapString.substring(1, mapString.length() - 1);           //remove curly brackets
        String[] keyValuePairs = mapString.split(",");              //split the string to creat key-value pairs
        Map<String, Object> map = new HashMap<>();

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=", 2);
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword64((String) map.get("Password"), (String) map.get("Salt")));

            Log.i("Data  ", map.get("Data").toString());

            byte[] textDecrypted = cipher.doFinal(toByte(map.get("Data").toString()));
            String result = new String(textDecrypted);

            return result;
        } catch (Exception e) {
            Log.i("Exception ", e.getMessage());
            return "Error";
        }
    }

    public Map<String, Object> decryptVitals(String mapString) {
        mapString = mapString.trim();
        mapString = mapString.substring(1, mapString.length() - 1);

        String[] keyValuePairs = mapString.split(",");              //split the string to creat key-value pairs
        Map<String, Object> map = new HashMap<>();

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=", 2);
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        Map<String, Object> returnMap = new HashMap<>();

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword64((String) map.get("Password"), (String) map.get("Salt")));

            byte[] textDecrypted = cipher.doFinal(toByte(map.get("Data").toString()));
            String result = new String(textDecrypted);

            result = result.substring(1, result.length() - 1);
            String[] resultKeyValuePairs = result.split(",");              //split the string to creat key-value pairs

            for (String pair : resultKeyValuePairs)                        //iterate over the pairs
            {
                String[] entry = pair.split(":");
                returnMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
            }

            return returnMap;
        } catch (Exception e) {
            Log.i("Exception while decrypting data", e.getMessage());
            return returnMap;
        }
    }
}
