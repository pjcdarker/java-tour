package com.pjcdarker.util;

public class IDCard {

    private static int[] wight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static char[] validCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    public static char getValidCode(String id) {
        int sum = 0;
        for (int i = 0, length = id.length(); i < length; i++) {
            String s = String.valueOf(id.charAt(i));
            sum = sum + Integer.parseInt(s) * wight[i];
        }
        int code = sum % 11;
        return validCodes[code];
    }

    public static void main(String[] args) {
        char validCode = IDCard.getValidCode("");
        System.out.println("last id card is " + validCode);
    }
}
