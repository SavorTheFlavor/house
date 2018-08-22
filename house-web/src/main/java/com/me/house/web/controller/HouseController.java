package com.me.house.web.controller;

import com.google.common.base.Strings;
import com.me.house.biz.service.AgencyService;
import com.me.house.biz.service.CityService;
import com.me.house.biz.service.HouseService;
import com.me.house.common.constant.CommonConstants;
import com.me.house.common.model.*;
import com.me.house.common.page.PageData;
import com.me.house.common.page.PageParams;
import com.me.house.common.result.ResultMsg;
import com.me.house.web.interceptor.UserContext;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Administrator on 2018/8/18.
 */
@Controller
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AgencyService agencyService;

    /**
     * 1.分页
     * 2.小区搜索、类型搜索
     * 3.排序
     * 4.展示图片、价格、标题、地址等信息
     * @return
     */
    @RequestMapping("/house/list")
    public String houseList(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            House queryCondition, ModelMap modelMap){
        PageData<House> houseList = houseService.queryHouse(queryCondition, PageParams.build(pageSize, pageNum));
        modelMap.put("ps", houseList);
        modelMap.put("vo", queryCondition);
        return "house/listing";
    }


    @RequestMapping("/house/toAdd")
    public String toAdd(ModelMap modelMap) {
        modelMap.put("citys", cityService.getAllCitys());
        modelMap.put("communitys", houseService.getAllCommunitys());
        return "/house/add";
    }

    @RequestMapping("/house/add")
    public String doAdd(House house){
        User user = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        houseService.addHouse(house,user);
        return "redirect:/house/ownlist";
    }

    @RequestMapping("house/ownlist")
    public String ownlist(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "own");
        return "/house/ownlist";
    }

    /**
     * 查询房屋详情
     * 查询关联经纪人
     * @param id
     * @return
     */
    @RequestMapping("house/detail")
    public String houseDetail(@Param("id") Long id, ModelMap modelMap){
        House house = houseService.queryOneHouse(id);
        HouseUser houseUser = houseService.getHouseUser(id);
//        recommendService.increase(id);
//        List<Comment> comments = commentService.getHouseComments(id,8);
        if (houseUser != null && houseUser.getUserId() != null && !houseUser.getUserId().equals(0)) {
            modelMap.put("agent", agencyService.getAgentDeail(houseUser.getUserId()));
        }
//        List<House> rcHouses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
//        modelMap.put("recomHouses", rcHouses);
        modelMap.put("house", house);
//        modelMap.put("commentList", comments);
        return "/house/detail";
    }

    @RequestMapping("house/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + ResultMsg.successMsg("留言成功").asUrlParams();
    }

}
