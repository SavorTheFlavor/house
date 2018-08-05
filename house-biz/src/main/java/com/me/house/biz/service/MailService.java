package com.me.house.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.me.house.biz.mapper.UserMapper;
import com.me.house.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/5.
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserMapper userMapper;

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

    @Value("${domain.name}")
    private String domainName;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String title, String verifyUrl, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setText("<a href=\"+" + verifyUrl + "+\">"+ verifyUrl +"(expire in 15minutes)</a>");
        message.setSubject(title);
        mailSender.send(message);
    }

    /**
     * 1.缓存key-email的关系
     * 2.借助spring mail 发送邮件
     * 3.借助异步框架进行异步操作(因为发邮件是一个比较耗时的操作)
     * @param email
     */
    @Async
    public void registerNotify(String email) {
        // Creates a random string whose length is the number of parameter 'count'
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        String verifyUrl = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        sendMail("house selling platform：account activation", verifyUrl, email);
    }

    public boolean enable(String key) {
        String email = registerCache.getIfPresent(key);
        if(StringUtils.isBlank(key)){
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setEnable(1);
        userMapper.update(user);
        registerCache.invalidate(key);
        return true;
    }
}
