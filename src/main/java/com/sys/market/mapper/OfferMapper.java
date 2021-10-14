package com.sys.market.mapper;

import com.sys.market.dto.criteria.OfferSearchCriteria;
import com.sys.market.entity.Offer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferMapper {
    Offer selectOfferById(Integer id);

    List<Offer> selectOfferList(OfferSearchCriteria criteria);

    int selectOfferCount(OfferSearchCriteria criteria);

    void insertOffer(Offer offerInfo);

    // itemId가 같은 모든 오퍼들의 isAccepted를 false로 isCompleted를 true로 변경한다.
    void updateOfferIsAccepted(Integer id, Boolean isAccepted);

    void updateOfferComplete(Integer itemId);
}
