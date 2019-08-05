package top.wifistar.utils;

import android.text.TextUtils;
import java.security.GeneralSecurityException;

/**
 * Created by boyla on 2019/8/1.
 */

public class DecryptUtil {

    public static String encrypt(String user1, String user2, String ciphertext) {
        if (TextUtils.isEmpty(ciphertext))
            return "";
        String result = "";
        try {
            result = AESCrypt.encrypt(getDecryptWord(user1, user2), ciphertext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String user1, String user2, String ciphertext) {
        if (TextUtils.isEmpty(ciphertext))
            return "";
        String result = "";
        try {
            result = AESCrypt.decrypt(getDecryptWord(user1, user2), ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDecryptWord(String id1, String id2) {
        if (TextUtils.isEmpty(id1) || TextUtils.isEmpty(id2)) {
            return "";
        }
        int len1 = id1.length();
        int len2 = id2.length();
        int len = len1 > len2 ? len2 : len1;
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            int a = (int) id1.charAt(i);
            int b = (int) id2.charAt(i);
//            System.out.println("value:" + a + "char:" + ((char) a) + "binstr" + Integer.toBinaryString(a));
//            System.out.println("value:" + b + "char:" + ((char) b) + "binstr" + Integer.toBinaryString(b));
            int res = (a ^ b) + 32;
            cs[i] = (char) res;
        }
        return new String(cs);
    }
}
