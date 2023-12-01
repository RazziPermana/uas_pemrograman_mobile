package com.example.uastest;
public class UserCacheUtil {
    private static User cachedUser;

    public static User getCachedUser() {
        return cachedUser;
    }

    public static void setCachedUser(User user) {
        cachedUser = user;
    }
    public static void clearCachedUser() {
        cachedUser = null;
    }
}
