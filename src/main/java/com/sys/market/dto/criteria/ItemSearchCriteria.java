package com.sys.market.dto.criteria;

import com.sys.market.enums.ItemSearchTarget;
import com.sys.market.enums.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemSearchCriteria extends Criteria {
    private ItemSearchTarget searchTarget;
    private String searchKeyword;
    private List<Integer> categoryIdList;
    private Integer maxPrice;
    private Integer minPrice;
    private List<ItemStatus> itemStatusList;
}
