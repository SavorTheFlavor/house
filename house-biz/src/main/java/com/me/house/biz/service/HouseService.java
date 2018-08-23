package com.me.house.biz.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.me.house.biz.mapper.HouseMapper;
import com.me.house.common.constant.HouseUserType;
import com.me.house.common.model.*;
import com.me.house.common.page.PageData;
import com.me.house.common.page.PageParams;
import com.me.house.common.utils.BeanHelper;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private FileService fileService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private MailService mailService;

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

    public List<Community> getAllCommunitys() {
        Community community = new Community();
        return houseMapper.selectCommunity(community);
    }

    /**
     * 添加房屋图片
     * 添加户型图片
     * 插入房产信息
     * 绑定用户到房产的关系
     * @param house
     * @param user
     */
    public void addHouse(House house, User user) {
        if (CollectionUtils.isNotEmpty(house.getHouseFiles())) {
            String images = Joiner.on(",").join(fileService.saveFiles(house.getHouseFiles()));
            house.setImages(images);
        }
        if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
            String images = Joiner.on(",").join(fileService.saveFiles(house.getFloorPlanFiles()));
            house.setFloorPlan(images);
        }
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        bindUser2House(house.getId(),user.getId(),false);
    }

    public void bindUser2House(Long houseId, Long userId, boolean collect) {
        HouseUser existhouseUser =     houseMapper.selectHouseUser(userId,houseId,collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
        if (existhouseUser != null) {
            return;
        }
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
        BeanHelper.setDefaultProp(houseUser, HouseUser.class);
        BeanHelper.onInsert(houseUser);
        houseMapper.insertHouseUser(houseUser);
    }

    public HouseUser getHouseUser(Long houseId){
        HouseUser houseUser =  houseMapper.selectSaleHouseUser(houseId);
        return houseUser;
    }

    public House queryOneHouse(Long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query, PageParams.build(1, 1));
        if (!houses.isEmpty()) {
            return houses.get(0);
        }
        return null;
    }

    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDetail(userMsg.getAgentId());
        mailService.sendMail("来自用户"+userMsg.getEmail()+"的留言", userMsg.getMsg(), agent.getEmail());
    }
}
