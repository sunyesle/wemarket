package com.sys.market.controller.api;

import com.sys.market.entity.Category;
import com.sys.market.dto.result.ListResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.service.CategoryService;
import com.sys.market.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseService responseService;

    @Operation(summary = "카테고리 조회")
    @GetMapping("/{id}")
    public SingleResult<Category> category(@PathVariable Integer id){
        return responseService.getSingleResult(categoryService.findCategory(id));
    }

    @Operation(summary = "카테고리 리스트 조회")
    @GetMapping
    public ListResult<Category> categoryList(){
        List<Category> categoryList = categoryService.findCategoryList();
        return responseService.getListResult(categoryList);
    }
}
