package com.sys.market.service;

import com.sys.market.entity.Category;
import com.sys.market.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;

    public Category findCategory(Integer id){
        return categoryMapper.selectCategory(id);
    }

    public List<Category> findCategoryList() {
        return categoryMapper.selectCategoryList();
    }
}
