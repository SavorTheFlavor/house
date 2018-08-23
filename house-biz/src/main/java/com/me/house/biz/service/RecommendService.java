package com.me.house.biz.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.me.house.common.model.House;
import com.me.house.common.page.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RecommendService {

  @Value("${myserver.host}")
  private String host;

  @Autowired
  private HouseService houseService;

  /* Redis key */
  private static final String HOT_HOUSE_KEY = "hot_house";

  private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);

  /**
   * 增加redis中保存热门房产zset的某房产id的score
   * @param id
   */
  public void increase(Long id){
    try {
      Jedis jedis = new Jedis(host, 6379);
      // zset
      jedis.zincrby(HOT_HOUSE_KEY, 1.00, id + "");
      // 只保留最热门的前十个
      jedis.zremrangeByRank(HOT_HOUSE_KEY, 10, -1);
      jedis.close();
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
  }

  public List<Long> getHot(){
    try {
      Jedis jedis = new Jedis(host, 6379);
      //查询出所有热门房产
      Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
      List<Long> idList = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
      return idList;
    }catch (Exception e){
      logger.error(e.getMessage(), e);
      return Lists.newArrayList();
    }
  }

  public List<House> getHotHouse(Integer size) {
    House query = new House();
    List<Long> list = getHot();
    list = list.subList(0, Math.min(list.size(), size));
    if(list.isEmpty()){
      return Lists.newArrayList();
    }
    query.setIds(list);
    final List<Long> idsOrderList = list;
    List<House> houses = houseService.queryAndSetImg(query, PageParams.build(size, 1));
    //使用onResultOf(function)的result来ordering
    Ordering<House> houseSort = Ordering.natural().onResultOf(house -> idsOrderList.indexOf(house.getId()));
    return houseSort.sortedCopy(houses);
  }

  public List<House> getLastest() {
    House query = new House();
    query.setSort("create_time");
    List<House> houses = houseService.queryAndSetImg(query, new PageParams(8, 1));
    return houses;
  }

}
