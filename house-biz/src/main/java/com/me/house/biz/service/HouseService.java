package com.me.house.biz.service;

import com.google.common.base.Strings;
import com.me.house.biz.mapper.HouseMapper;
import com.me.house.common.model.Community;
import com.me.house.common.model.House;
import com.me.house.common.page.PageData;
import com.me.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Administrator on 2018/8/18.
 */
@Service
public class HouseService {

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private HouseMapper houseMapper;

    public PageData<House> queryHouse(House queryCondition, PageParams page) {
        if(!Strings.isNullOrEmpty(queryCondition.getName())){
            Community community = new Community();
            community.setName(queryCondition.getName());
            List<Community> communityList = houseMapper.selectCommunity(community);
            if(!communityList.isEmpty()){
                queryCondition.setCommunityId(communityList.get(0).getId());
            }
        }
        List<House> houseList = queryAndSetImg(queryCondition, page);
        Long count = houseMapper.selectRecordsCount(queryCondition);
        return PageData.buildPage(houseList, count, page.getPageSize(), page.getPageNum());
    }

    private List<House> queryAndSetImg(House queryCondition, PageParams page) {
        List<House> houseList = houseMapper.selectHouseByPage(queryCondition, page);
        houseList.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(pic -> imgPrefix + pic).collect(Collectors.toList()));
        });
        return houseList;
    }
}
