package com.me.house.biz.mapper;

import com.me.house.common.model.Community;
import com.me.house.common.model.House;
import com.me.house.common.model.HouseUser;
import com.me.house.common.model.UserMsg;
import com.me.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/8/18.
 */
@Mapper
public interface HouseMapper {
    List<House> selectHouseByPage(@Param("house") House queryCondition, @Param("page") PageParams page);

    Long selectRecordsCount(@Param("house") House queryCondition); //必须添加@Param("house") ，不然在mapper.xml中使用house.id什么的就不知道这个house是什么了

    int insert(House house);

    List<Community> selectCommunity(Community community);

    HouseUser selectHouseUser(@Param("userId")Long userId, @Param("id") Long houseId, @Param("type") Integer integer);

    HouseUser selectSaleHouseUser(@Param("id") Long houseId);

    int insertHouseUser(HouseUser houseUser);

    int insertUserMsg(UserMsg userMsg);

    int updateHouse(House updateHouse);

     int downHouse(Long id);

    int deleteHouseUser(@Param("id")Long id,@Param("userId") Long userId,@Param("type") Integer value);

}
