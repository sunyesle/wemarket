package com.sys.market.dto.criteria;

import com.sys.market.enums.OfferAccepted;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferSearchCriteria extends Criteria{
    Integer itemId;
    String sellerId;
    String buyerId;
    OfferAccepted accepted;
    Boolean completed;
}
