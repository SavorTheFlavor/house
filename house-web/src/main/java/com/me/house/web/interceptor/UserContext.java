package com.me.house.web.interceptor;

import com.me.house.common.model.User;

/**
 * Created by Administrator on 2018/8/11.
 */
public class UserContext {
    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(User user){
        USER_HOLDER.set(user);
    }

    public static void remove(){
        USER_HOLDER.remove();
    }

    public static User getUser(){
        return USER_HOLDER.get();
    }
}
