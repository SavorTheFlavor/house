package com.me.houses.mapper;

import com.me.houses.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
@Mapper
public interface UserMapper {
    public List<User> selectUsers();
}
