package com.sys.market.controller.api;


import com.jayway.jsonpath.JsonPath;
import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.Item;
import com.sys.market.enums.ItemSearchTarget;
import com.sys.market.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//파일 업로드 부분은 제외하고 테스트
class ItemControllerTest extends BaseIntegrationTest {
    @Autowired
    private ItemService itemService;

    @Test
    void 아이템_조회() throws Exception{
        //given
        Item itemInfo = itemSetup();

        //when
        ResultActions resultActions = mvc.perform(get("/api/items/"+itemInfo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(itemInfo.getId()))
                .andExpect(jsonPath("$.data.userId").value("user"))
                .andExpect(jsonPath("$.data.categoryId").value(itemInfo.getCategoryId()))
                .andExpect(jsonPath("$.data.price").value(itemInfo.getPrice()))
                .andExpect(jsonPath("$.data.title").value(itemInfo.getTitle()))
                .andExpect(jsonPath("$.data.content").value(itemInfo.getContent()));
    }

    @Test
    void 아이템_리스트_조회() throws Exception{
        for(int i = 0; i < 15; i++){
            Item itemInfo = new Item();
            itemInfo.setUserId("user");
            itemInfo.setCategoryId(0);
            itemInfo.setPrice(i);
            itemInfo.setTitle((i+1) + "번째 아이템");
            itemInfo.setContent("내용");
            itemService.addItem(itemInfo);
        }

        //파라미터 없이. 페이징 기본값
        ResultActions resultActions = mvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(10));

        //페이징
        resultActions = mvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("page", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(5));

        //검색
        resultActions = mvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("searchTarget", ItemSearchTarget.TITLE_CONTENT.name())
                .param("searchKeyword", "2번")
                .param("maxPrice", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));

        //검색결과 없음
        resultActions = mvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("searchTarget", ItemSearchTarget.TITLE.name())
                .param("searchKeyword", "없음")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.list.length()").value(0));
    }

    @Test
    void 아이템_추가() throws Exception{
        ItemController.ItemRequest itemRequest = new ItemController.ItemRequest();
        itemRequest.setCategoryId(1);
        itemRequest.setPrice(10000);
        itemRequest.setTitle("팝니다");
        itemRequest.setContent("쿨거래 원해요");

        ResultActions resultActions = mvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessCookie)
                .content(objectMapper.writeValueAsString(itemRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isCreated());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        Integer itemId = ((Number) JsonPath.parse(content).read("$.data.id")).intValue();

        Item item = itemService.findItem(itemId);

        then(item.getId()).isEqualTo(itemId);
        then(item.getCategoryId()).isEqualTo(itemRequest.getCategoryId());
        then(item.getPrice()).isEqualTo(itemRequest.getPrice());
        then(item.getTitle()).isEqualTo(itemRequest.getTitle());
        then(item.getContent()).isEqualTo(itemRequest.getContent());
    }

    @Test
    void 아이템_수정() throws Exception{
        Item itemInfo = itemSetup();

        ItemController.ItemRequest itemRequest = new ItemController.ItemRequest();
        itemRequest.setCategoryId(2);
        itemRequest.setPrice(5000);
        itemRequest.setTitle("삽니다");
        itemRequest.setContent("수정");

        ResultActions resultActions = mvc.perform(put("/api/items/"+itemInfo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessCookie)
                .content(objectMapper.writeValueAsString(itemRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Item item = itemService.findItem(itemInfo.getId());
        then(item.getCategoryId()).isEqualTo(itemRequest.getCategoryId());
        then(item.getPrice()).isEqualTo(itemRequest.getPrice());
        then(item.getTitle()).isEqualTo(itemRequest.getTitle());
        then(item.getContent()).isEqualTo(itemRequest.getContent());
    }

    @Test
    void 아이템_삭제() throws Exception{
        Item itemInfo = itemSetup();

        ResultActions resultActions = mvc.perform(delete("/api/items/"+itemInfo.getId())
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        resultActions.andExpect(status().isOk());
        Item item = itemService.findItem(itemInfo.getId());
        then(item).isNull();
    }

    public Item itemSetup(){
        Item itemInfo = new Item();
        itemInfo.setUserId("user");
        itemInfo.setCategoryId(0);
        itemInfo.setPrice(10000);
        itemInfo.setTitle("팝니다");
        itemInfo.setContent("쿨거래 원해요");
        Integer itemId = itemService.addItem(itemInfo);
        return itemService.findItem(itemId);
    }
}
