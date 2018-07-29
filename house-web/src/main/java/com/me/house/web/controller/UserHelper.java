package com.me.house.web.controller;

import com.me.house.common.model.User;
import com.me.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2018/7/29.
 */
public class UserHelper {
    public static ResultMsg validate(User account) {
        if (StringUtils.isBlank(account.getEmail())) {
            return ResultMsg.errorMsg("Email 有误");
        }
        if (StringUtils.isBlank(account.getName())) {
            return ResultMsg.errorMsg("账号名称 有误");
        }
        if (StringUtils.isBlank(account.getConfirmPasswd()) || StringUtils.isBlank(account.getPasswd())
                || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码 有误");
        }
        if (account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码需大于5位");
        }
        return ResultMsg.successMsg("");
    }
}
