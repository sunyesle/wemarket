package com.sys.market.dto.criteria;

import com.sys.market.enums.ReviewType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchCriteria extends Criteria{
    private String userId;
    private String writerId;
    private ReviewType reviewType;
}
