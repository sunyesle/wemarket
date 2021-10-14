package com.sys.market.mapper;

import com.sys.market.entity.ItemImage;

import java.util.List;

public interface ItemImageMapper {
    List<String> selectItemImagePathList(Integer itemId);

    List<Integer> selectItemImageIdList(Integer itemId);

    void insertItemImages(List<ItemImage> itemImageList);

    void updateItemImages(List<ItemImage> itemImageList);

    void deleteItemImageByItemId(Integer itemId);

    void deleteItemImages(List<Integer> idList);
}
