package com.me.house.biz.service;

import com.google.common.collect.Lists;
import com.me.house.biz.mapper.UserMapper;
import com.me.house.common.model.User;
import com.me.house.common.utils.BeanHelper;
import com.me.house.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileService fileService;

    public List<User> getUsers() {
        return userMapper.selectUsers();
    }

    /**
     * 1.插入数据库，非激活；密码加盐md5；保存头像到本地
     * 2.生成key，绑定email
     * 3.发送邮件给用户
     * @param account
     * @return
     */
    @Transactional(rollbackFor = Exception.class) //notice：在这个类内部调用这个方法的话，事务是不生效的；因为spring需要通过反射读取该注解，并通过AOP添加事务控制
    public boolean addAccount(User account) {
        account.setPasswd(HashUtils.encryptPassword(account.getPasswd()));
        List<String> fileList = fileService.saveFiles(Lists.newArrayList(account.getAvatarFile()));
        if(!fileList.isEmpty()){
            account.setAvatar(fileList.get(0));
        }
        //插入前为其设置默认值
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        registerNotify(account.getEmail());
        return false;
    }
}
