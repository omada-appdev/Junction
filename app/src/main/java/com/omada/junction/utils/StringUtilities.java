package com.omada.junction.utils;

import java.util.Random;

public class StringUtilities {

    public static String randomStringGenerator(int size, boolean includeLetters,
                                               boolean includeNumbers, String specialCharacters){

        String SALTCHARS = "";
        if(includeLetters){
            SALTCHARS += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        }
        if(includeNumbers){
            SALTCHARS += "1234567890";
        }
        if(specialCharacters != null){
            SALTCHARS += specialCharacters;
        }

        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    public static String randomNumberGenerator(int size){
        return randomStringGenerator(size, false, true, null);
    }

    public static String randomAlphabetGenerator(int size){
        return randomStringGenerator(size, true, false, null);
    }

    public static String randomAlphaNumericGenerator(int size) {
        return randomStringGenerator(size, true, true, null);
    }
}
