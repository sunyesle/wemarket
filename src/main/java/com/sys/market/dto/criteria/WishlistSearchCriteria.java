package com.sys.market.dto.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistSearchCriteria extends Criteria{
    private String userId;
    private Integer itemId;
}
