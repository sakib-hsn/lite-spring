package com.sakib.io.litespring.context;

import com.sakib.io.models.User;

public class UserContext {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUserContext(User user) {
        userThreadLocal.set(user);
    }

    public static User getUserContext() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
