package com.pjcdarker.base.util;

/**
 * @author pjcdarker
 */
public class CardUtil {

    private static int[] wight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static char[] validCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    public static char getLastCodeOfIdCard(String id) {
        int sum = 0;
        for (int i = 0, length = id.length(); i < length; i++) {
            String s = String.valueOf(id.charAt(i));
            sum = sum + Integer.parseInt(s) * wight[i];
        }
        int code = sum % 11;
        return validCodes[code];
    }

    public static void main(String[] args) {
        char validCode = getLastCodeOfIdCard("");
        System.out.println("last id card is " + validCode);
    }

    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
