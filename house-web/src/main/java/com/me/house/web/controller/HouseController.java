package com.me.house.web.controller;

import com.google.common.base.Strings;
import com.me.house.biz.service.HouseService;
import com.me.house.common.model.Community;
import com.me.house.common.model.House;
import com.me.house.common.page.PageData;
import com.me.house.common.page.PageParams;
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

}
