package com.saas.sso.auth.resource.security.crsf;

import java.util.Random;
import java.util.UUID;

/**
 * @author feng
 * @date 2019/12/12
 */
// TODO CRSF Token算法
public class CRSFUtils {

    public static String generateCRSFTokenSalt() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            result.append(
                    chars.charAt(
                            random.nextInt(chars.length())
                    )
            );
        }
        return result.toString();
    }

    public static String generateOriginalCRSFToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateCRSFToken(String original, String salt) {
        // todo
        return original + salt;
    }

    public static boolean checkCRSFToken(String original, String salt, String CRSFToken) {
        return CRSFToken.equals(generateCRSFToken(original, salt));
    }
}
