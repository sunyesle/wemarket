package com.sys.market.controller.api;


import com.jayway.jsonpath.JsonPath;
import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.Item;
import com.sys.market.entity.Offer;
import com.sys.market.entity.User;
import com.sys.market.enums.ItemStatus;
import com.sys.market.enums.UserRole;
import com.sys.market.service.ItemService;
import com.sys.market.service.OfferService;
import com.sys.market.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OfferControllerTest extends BaseIntegrationTest {
    @Autowired
    private OfferService offerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Test
    void 오퍼_조회() throws Exception{
        userSetup("seller");

        Item itemInfo = itemSetup("seller");
        Offer offerInfo = offerSetup(itemInfo, "user");

        ResultActions resultActions = mvc.perform(get("/api/offers/"+offerInfo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(offerInfo.getId()))
                .andExpect(jsonPath("$.data.itemId").value(offerInfo.getItemId()))
                .andExpect(jsonPath("$.data.sellerId").value(offerInfo.getSellerId()))
                .andExpect(jsonPath("$.data.buyerId").value(offerInfo.getBuyerId()))
                .andExpect(jsonPath("$.data.price").value(offerInfo.getPrice()));
    }

    @Test
    void 오퍼_리스트_조회() throws Exception {
        userSetup("seller");

        Item itemInfo = itemSetup("seller");
        for(int i = 0; i < 5; i++){
            userSetup("anotherUser"+i);
            offerSetup(itemInfo, "anotherUser"+i);
        }
        Offer offerInfo = offerSetup(itemInfo, "user");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("itemId", itemInfo.getId().toString());

        ResultActions resultActions = mvc.perform(get("/api/offers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(6));

        // 검색
        params.clear();
        params.add("itemId", itemInfo.getId().toString());
        params.add("sellerId", offerInfo.getSellerId());
        params.add("buyerId", offerInfo.getBuyerId());

        resultActions = mvc.perform(get("/api/offers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].id").value(offerInfo.getId().toString()));

        // 결과 없을 때
        params.clear();
        params.add("buyerId", "anotherUser");

        resultActions = mvc.perform(get("/api/offers")
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
    void 오퍼하기() throws Exception{
        userSetup("seller");
        Item itemInfo = itemSetup("seller");

        OfferController.OfferRequest offerRequest = new OfferController.OfferRequest();
        offerRequest.setItemId(itemInfo.getId());
        offerRequest.setPrice(9000);

        ResultActions resultActions = mvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest))
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isCreated());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        Integer offerId = ((Number) JsonPath.parse(content).read("$.data.id")).intValue();
        Offer offer = offerService.findOffer(offerId);

        then(offer.getItemId()).isEqualTo(offerRequest.getItemId());
        then(offer.getPrice()).isEqualTo(offerRequest.getPrice());
        then(offer.getBuyerId()).isEqualTo("user");
        then(offer.getSellerId()).isEqualTo(itemInfo.getUserId());
        then(offer.getIsAccepted()).isNull();
        then(offer.getIsCompleted()).isFalse();
    }

    @Test
    void 완료상태인_아이템에_오퍼하면_실패() throws Exception{
        userSetup("seller");
        userSetup("buyer");
        Item itemInfo = itemSetup("seller");
        Offer offerInfo = offerSetup(itemInfo, "buyer");
        offerService.completeOffer(offerInfo);

        OfferController.OfferRequest offerRequest = new OfferController.OfferRequest();
        offerRequest.setItemId(itemInfo.getId());
        offerRequest.setPrice(9000);

        ResultActions resultActions = mvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest))
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void 오퍼_수락_아이템의_판매자일때() throws Exception{
        userSetup("buyer");
        Item itemInfo = itemSetup("user");
        Offer offerInfo = offerSetup(itemInfo, "buyer");

        ResultActions resultActions = mvc.perform(put("/api/offers/" + offerInfo.getId() + "/accept")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Offer offer = offerService.findOffer(offerInfo.getId());
        then(offer.getIsAccepted()).isTrue();

        Item item = itemService.findItem(offerInfo.getItemId());
        then(item.getStatus()).isEqualTo(ItemStatus.TRADE);
    }

    @Test
    void 오퍼_수락_아이템의_판매자가_아닐때_실패() throws Exception{
        userSetup("anotherUser");
        userSetup("buyer");
        Item itemInfo = itemSetup("anotherUser");
        Offer offerInfo = offerSetup(itemInfo, "buyer");

        ResultActions resultActions = mvc.perform(put("/api/offers/" + offerInfo.getId() + "/accept")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void 오퍼_취소_판매자일때() throws Exception{
        userSetup("buyer");
        Item itemInfo = itemSetup("user");
        Offer offerInfo = offerSetup(itemInfo, "buyer");
        offerService.acceptOffer(offerInfo);

        ResultActions resultActions = mvc.perform(put("/api/offers/" + offerInfo.getId() + "/cancel")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Offer offer = offerService.findOffer(offerInfo.getId());
        then(offer.getIsAccepted()).isFalse();

        Item item = itemService.findItem(offerInfo.getItemId());
        then(item.getStatus()).isEqualTo(ItemStatus.OPEN);
    }

    @Test
    void 오퍼_취소_구매자일때() throws Exception{
        userSetup("seller");
        Item itemInfo = itemSetup("seller");
        Offer offerInfo = offerSetup(itemInfo, "user");
        offerService.acceptOffer(offerInfo);

        ResultActions resultActions = mvc.perform(put("/api/offers/" + offerInfo.getId() + "/cancel")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Offer offer = offerService.findOffer(offerInfo.getId());
        then(offer.getIsAccepted()).isFalse();

        Item item = itemService.findItem(offerInfo.getItemId());
        then(item.getStatus()).isEqualTo(ItemStatus.OPEN);
    }

    @Test
    void 오퍼_완료_판매자일때() throws Exception{
        userSetup("buyer");
        Item itemInfo = itemSetup("user");
        Offer offerInfo = offerSetup(itemInfo, "buyer");
        offerService.acceptOffer(offerInfo);

        ResultActions resultActions =  mvc.perform(put("/api/offers/" + offerInfo.getId() + "/complete")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Offer offer = offerService.findOffer(offerInfo.getId());
        then(offer.getIsCompleted()).isTrue();

        Item item = itemService.findItem(offerInfo.getItemId());
        then(item.getStatus()).isEqualTo(ItemStatus.COMPLETED);
    }

    @Test
    void 오퍼_완료_구매자일때() throws Exception{
        userSetup("seller");
        Item itemInfo = itemSetup("seller");
        Offer offerInfo = offerSetup(itemInfo, "user");
        offerService.acceptOffer(offerInfo);

        ResultActions resultActions =  mvc.perform(put("/api/offers/" + offerInfo.getId() + "/complete")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk());

        Offer offer = offerService.findOffer(offerInfo.getId());
        then(offer.getIsCompleted()).isTrue();

        Item item = itemService.findItem(offerInfo.getItemId());
        then(item.getStatus()).isEqualTo(ItemStatus.COMPLETED);
    }


    public Offer offerSetup(Item item, String buyerId){
        Offer offerInfo = new Offer();
        offerInfo.setBuyerId(buyerId);
        offerInfo.setSellerId(item.getUserId());
        offerInfo.setItemId(item.getId());
        offerInfo.setPrice(9000);
        Integer offerId = offerService.saveOffer(offerInfo);
        return offerService.findOffer(offerId);
    }

    public Item itemSetup(String userId){
        Item itemInfo = new Item();
        itemInfo.setUserId(userId);
        itemInfo.setCategoryId(0);
        itemInfo.setPrice(10000);
        itemInfo.setTitle("팝니다");
        itemInfo.setContent("쿨거래 원해요");
        Integer itemId = itemService.addItem(itemInfo);
        return itemService.findItem(itemId);
    }

    public void userSetup(String id){
        User userInfo = User.builder()
                .id(id)
                .password("passw0rd")
                .nickname("nickname")
                .addressCode("1111010100")
                .email("email@email.com")
                .role(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();
        userService.addUser(userInfo);
    }
}
