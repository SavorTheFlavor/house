package com.me.house.biz.service;

import com.me.house.biz.mapper.UserMapper;
import com.me.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.selectUsers();
    }

    public boolean addAccount(User account) {
        //TODO
        return false;
    }
}
