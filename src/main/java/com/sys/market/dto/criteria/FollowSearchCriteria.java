package com.sys.market.dto.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowSearchCriteria extends Criteria {
    private String userId;
    private String followerId;
}
