package com.me.houses.controller;

import com.me.houses.model.User;
import com.me.houses.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public @ResponseBody List<User> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping("/hello")
    public String hello(ModelMap modelMap){
        modelMap.put("user", userService.getUsers().get(0));
        return "hello";
    }
}
