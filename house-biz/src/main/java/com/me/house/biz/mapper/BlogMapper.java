package com.me.house.biz.mapper;

import java.util.List;

import com.me.house.common.model.Blog;
import com.me.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface BlogMapper {

  public List<Blog> selectBlog(@Param("blog") Blog query, @Param("pageParams") PageParams params);

  public Long selectBlogCount(Blog query);

}
