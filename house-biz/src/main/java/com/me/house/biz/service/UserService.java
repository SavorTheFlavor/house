package com.me.house.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import com.me.house.biz.mapper.UserMapper;
import com.me.house.common.model.User;
import com.me.house.common.utils.BeanHelper;
import com.me.house.common.utils.HashUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/22.
 */
@Service
public class UserService {

    private final Cache<String, String> registerCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    //超时仍未激活则将其从数据库中删除
                    userMapper.delete(notification.getValue());
                }
            }).build();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private MailService mailService;

    @Value("${domain.name}")
    private String domainName;

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

    /**
     * 1.缓存key-email的关系
     * 2.借助spring mail 发送邮件
     * 3.借助异步框架进行异步操作(因为发邮件是一个比较耗时的操作)
     * @param email
     */
    @Async
    private void registerNotify(String email) {
        // Creates a random string whose length is the number of parameter 'count'
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        String verifyUrl = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        mailService.sendMail("house selling platform：account activation", verifyUrl, email);
    }
}
