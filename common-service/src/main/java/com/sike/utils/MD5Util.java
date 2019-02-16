package com.sike.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {

    /**
     * 加盐加密
     *
     * @param username
     * @param password
     * @param salt
     * @return
     */
    public static String entrypt(String username, String password, String salt) {
        return DigestUtils.md5DigestAsHex((username + salt + password).getBytes());
    }
}
