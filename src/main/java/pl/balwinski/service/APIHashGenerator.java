package pl.balwinski.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public abstract class APIHashGenerator {

    public static String generate(String data, String key) {

        String result = "";

        try{
            byte [] byteKey = key.getBytes(StandardCharsets.UTF_8);
            final String HMAC_SHA512 = "HmacSHA512";
            Mac sha512_HMAC;
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte [] mac_data = sha512_HMAC.
                    doFinal(data.getBytes(StandardCharsets.UTF_8));
            result = bytesToHex(mac_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}

