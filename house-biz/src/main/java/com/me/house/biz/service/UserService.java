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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.prefix}")
    private String imgPrefixPath;

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
        mailService.registerNotify(account.getEmail()); //这方法带有@Async注解，使用到了AOP，要使注解生效，必须将该方法放到其他类中
        return true;
    }

    /**
     * 激活key关联的邮箱账号
     * @param key
     * @return
     */
    public boolean enable(String key) {
        return mailService.enable(key);
    }

    /**
     * username and password authorizes
     * @param username
     * @param password
     * @return
     */
    public User auth(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setPasswd(HashUtils.encryptPassword(password));
        user.setEnable(1);
        List<User> list = getUserByCondition(user);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<User> getUserByCondition(User user) {
        List<User> list = userMapper.selectByUserCondition(user);
        list.forEach(u -> u.setAvatar(imgPrefixPath + u.getAvatar()));
        return list;
    }

    public void updateUser(User user, String email) {
        user.setEmail(email);
        BeanHelper.onUpdate(user);
        userMapper.update(user);
    }


    public User getUserById(Long id) {
        User queryUser = new User();
        queryUser.setId(id);
        List<User> users = getUserByCondition(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public void resetNotify(String username) {
        mailService.resetNotify(username);
    }

    /**
     * 重置密码操作
     * @param password
     * @param key
     */
    @Transactional(rollbackFor=Exception.class)
    public User reset(String key,String password){
        String email = getResetEmail(key);
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setPasswd(HashUtils.encryptPassword(password));
        userMapper.update(updateUser);
        mailService.invalidateRestKey(key);
        return getUserByEmail(email);
    }


    public User getUserByEmail(String email) {
        User queryUser = new User();
        queryUser.setEmail(email);
        List<User> users = getUserByCondition(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public String getResetEmail(String key) {
        String email = "";
        try {
            email =  mailService.getResetEmail(key);
        } catch (Exception ignore) {
        }
        return email;
    }


}
