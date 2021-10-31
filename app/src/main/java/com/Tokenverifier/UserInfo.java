package com.Tokenverifier;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    public static String
            id,
            username,
            password,
            coin,
            level,
            exp,
            info,
            token;
    public static boolean loginSucceed, isServerAvailable;
}
