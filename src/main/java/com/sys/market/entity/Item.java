package com.sys.market.entity;

import com.sys.market.controller.api.ItemController;
import com.sys.market.enums.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private Integer id;
    private String userId;
    private Integer categoryId;
    private Integer thumbImageId;
    private Integer price;
    private String title;
    private String content;
    private Date createDate;
    private Date updateDate;
    private Date deleteDate;
    private ItemStatus status;

    private String nickname;
    private String address;

    private String category;
    private Integer offerCount;

    private String thumbImage; // itemList 반환용

    private List<String> images; // itemDetail 반환용

    public ItemController.ItemDetailResponse toItemDetailResponse(){
        ItemController.ItemDetailResponse dto = new ItemController.ItemDetailResponse();
        dto.setId(this.id);
        dto.setUserId(this.userId);
        dto.setCategoryId(this.categoryId);
        dto.setPrice(this.price);
        dto.setTitle(this.title);
        dto.setContent(this.content);
        dto.setCreateDate(this.createDate);
        dto.setUpdateDate(this.updateDate);
        dto.setStatus(this.status);

        dto.setCategory(this.category);
        dto.setOfferCount(this.offerCount);
        dto.setImages(this.images);

        return dto;
    }

    public ItemController.ItemResponse toItemResponse(){
        ItemController.ItemResponse dto = new ItemController.ItemResponse();
        dto.setId(this.id);
        dto.setUserId(this.userId);
        dto.setCategoryId(this.categoryId);
        dto.setPrice(this.price);
        dto.setTitle(this.title);
        dto.setCreateDate(this.createDate);
        dto.setUpdateDate(this.updateDate);
        dto.setStatus(this.status);

        dto.setNickname(this.nickname);
        dto.setAddress(this.address);
        dto.setOfferCount(this.offerCount);
        dto.setCategory(this.category);
        dto.setThumbImage(this.thumbImage);

        return dto;
    }
}
