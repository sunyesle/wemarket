package com.sys.market.controller.api;

import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.Item;
import com.sys.market.service.ItemService;
import com.sys.market.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WishlistControllerTest extends BaseIntegrationTest {
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private ItemService itemService;

    @Test
    void 위시아이템_리스트_조회() throws Exception{
        Item item1 = itemSetup();
        Item item2 = itemSetup();
        wishlistService.addWishlist("user", item1.getId());
        wishlistService.addWishlist("user", item2.getId());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user");

        ResultActions resultActions = mvc.perform(get("/api/wishlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(2));

        params.clear();
        params.add("itemId", item1.getId().toString());

        resultActions = mvc.perform(get("/api/wishlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));

        params.clear();
        params.add("userId", "noContent");

        resultActions = mvc.perform(get("/api/wishlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.list.length()").value(0));
    }

    @Test
    void 위시아이템_추가() throws Exception{
        Item item = itemSetup();
        WishlistController.WishlistRequest wishlistRequest = new WishlistController.WishlistRequest();
        wishlistRequest.setItemId(item.getId());

        ResultActions resultActions = mvc.perform(post("/api/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishlistRequest))
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isCreated());
        then(wishlistService.wishlistExists("user", item.getId())).isTrue();
    }

    @Test
    void 위시아이템_삭제() throws Exception{
        Item item = itemSetup();
        wishlistService.addWishlist("user", item.getId());

        ResultActions resultActions = mvc.perform(delete("/api/wishlists/" + item.getId())
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());
        then(wishlistService.wishlistExists("user", item.getId())).isFalse();
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