package com.sys.market.mapper;

import com.sys.market.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryList();

    Category selectCategory(Integer id);
}
