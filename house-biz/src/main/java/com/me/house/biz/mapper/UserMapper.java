package com.me.house.biz.mapper;

import com.me.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
@Mapper
public interface UserMapper {
    List<User> selectUsers();

    int insert(User account);

    void delete(@Param("email") String email);
}
