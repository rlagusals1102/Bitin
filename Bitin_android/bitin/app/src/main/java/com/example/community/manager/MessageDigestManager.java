package com.example.community.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestManager
{
    private MessageDigest instance;

    public MessageDigestManager() {

    }

    private MessageDigestManager(java.security.MessageDigest instance) {
        this.instance = instance;
    }

    public static MessageDigestManager getInstance(String algorithm) throws NoSuchAlgorithmException {
        if (algorithm == null) {
            return null;
        }

        try {
            if (algorithm.equals("SHA-1"))
                return (MessageDigestManager) Class.forName("android.security.Sha1MessageDigest").newInstance();
            else if (algorithm.equals("MD5"))
                return (MessageDigestManager) Class.forName("android.security.Md5MessageDigest").newInstance();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return new MessageDigestManager(java.security.MessageDigest.getInstance(algorithm));
    }

    public void update(byte[] input) {
        instance.update(input);
    }

    public byte[] digest() {
        return instance.digest();
    }

    public byte[] digest(byte[] input) {
        return instance.digest(input);
    }
}
