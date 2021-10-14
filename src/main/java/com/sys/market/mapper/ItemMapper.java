package com.sys.market.mapper;

import com.sys.market.dto.criteria.ItemSearchCriteria;
import com.sys.market.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper {
    Item findItemById(Integer id);

    List<Item> selectItemList(ItemSearchCriteria criteria);

    int selectItemCount(ItemSearchCriteria criteria);

    void insertItem(Item itemInfo);

    void updateItem(Item item);

    void updateItemThumbImageId(Integer id);

    void deleteItem(Integer id);
}
