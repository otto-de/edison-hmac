package de.otto.edison.hmac;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class HmacSignatureCalculator {

    public HmacSignatureInfo calculateSignature(String url, String user, String password, String requestMethod, ZonedDateTime dateTime) {
        return calculateSignature(url, user, password, new byte[0], requestMethod, dateTime, false);
    }

    public HmacSignatureInfo calculateSignature(String url, String user, String password, byte[] body, String requestMethod, ZonedDateTime dateTime) {
        return calculateSignature(url, user, password, body, requestMethod, dateTime, false);
    }

    public HmacSignatureInfo calculateSignature(String url, String user, String password, byte[] body, String requestMethod, ZonedDateTime dateTime, boolean simpleSign) {
        String dateHeaderValue = DateTimeFormatter.ISO_INSTANT.format(dateTime);
        String stringToSign = simpleSign ? createSimpleStringToSign(requestMethod, url, dateHeaderValue) : createStringToSign(requestMethod, dateHeaderValue, url, body);
        String signature = signString(password, stringToSign);

        String signatureHeaderValue = user + ":" + signature;
        return new HmacSignatureInfo(signatureHeaderValue, dateHeaderValue);
    }

    private String signString(String secretKey, String stringToSign) {
        try {
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            final Mac mac;
            mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            final byte[] result = mac.doFinal(stringToSign.getBytes());
            return Base64.getEncoder().encodeToString(result).trim();
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String createStringToSign(final String method, final String dateHeader, final String requestUri, byte[] body) {
        final StringBuilder builder = new StringBuilder();

        builder.append(method).append("\n");
        builder.append(dateHeader).append("\n");
        builder.append(requestUri).append("\n");
        builder.append(toMd5Hex(body));

        return builder.toString();
    }

    private String createSimpleStringToSign(final String method, final String requestUri, final String dateHeader) {
        final StringBuilder builder = new StringBuilder();

        builder.append(method).append(requestUri).append(dateHeader);
        return builder.toString();
    }

    private String toMd5Hex(byte[] byteSource) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(byteSource);
            StringBuilder hexStringBuilder = toHexString(md5Bytes);

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("error evaluating md5 sum", e);
        }
    }

    private StringBuilder toHexString(byte[] md5Bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : md5Bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder;
    }
}
