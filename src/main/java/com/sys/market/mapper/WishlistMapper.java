package com.sys.market.mapper;

import com.sys.market.entity.Item;
import com.sys.market.dto.criteria.WishlistSearchCriteria;

import java.util.List;

public interface WishlistMapper {
    List<Item> selectWishlistList(WishlistSearchCriteria criteria);

    int selectWishlistCount(WishlistSearchCriteria criteria);

    void insertWishlist(String userId, Integer itemId);

    void deleteWishlist(String userId, Integer itemId);
}
