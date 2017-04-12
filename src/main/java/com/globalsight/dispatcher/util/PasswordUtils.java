package com.globalsight.dispatcher.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    private final static String SALT = "MT enhances localization process";
    private static final String INVALID_CHARACTERS = "<>\"'& ";

    public static boolean isValidPassword(String candidate) {
        if(null == candidate) return false;
        if(candidate.length() < 7 || candidate.length() > 25) {
            return false;
        }
        for (int i = 0; i < candidate.length(); i++)
        {
            if (INVALID_CHARACTERS.indexOf(candidate.charAt(i)) != -1)
            {
                return false;
            }
        }
        return true;
    }

    public static String md5(String input) {

        String md5 = null;

        if(null == input) return null;

        input = SALT + input;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }
}
