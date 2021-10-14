package com.sys.market.service;

import com.sys.market.entity.Item;
import com.sys.market.dto.criteria.WishlistSearchCriteria;
import com.sys.market.mapper.WishlistMapper;
import com.sys.market.util.file.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistMapper wishlistMapper;
    private final ImageFileUtil fileUtil;

    public List<Item> findWishlistList(WishlistSearchCriteria criteria) {
        List<Item> items = wishlistMapper.selectWishlistList(criteria);

        for(Item item : items){
            item.setThumbImage(fileUtil.nameToThumbName(item.getThumbImage()));
        }
        return items;
    }

    public int findWishlistCount(WishlistSearchCriteria criteria) {
        return wishlistMapper.selectWishlistCount(criteria);
    }

    public void addWishlist(String userId, Integer itemId) {
        wishlistMapper.insertWishlist(userId, itemId);
    }

    public void removeWishlist(String userId, Integer itemId) {
        wishlistMapper.deleteWishlist(userId, itemId);
    }

    public boolean wishlistExists(String userId, Integer itemId) {
        WishlistSearchCriteria criteria = new WishlistSearchCriteria();
        criteria.setUserId(userId);
        criteria.setItemId(itemId);
        int count = wishlistMapper.selectWishlistCount(criteria);
        return count>0;
    }
}
