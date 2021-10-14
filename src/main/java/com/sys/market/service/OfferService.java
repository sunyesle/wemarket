package com.sys.market.service;

import com.sys.market.dto.criteria.OfferSearchCriteria;
import com.sys.market.entity.Item;
import com.sys.market.entity.Offer;
import com.sys.market.enums.ItemStatus;
import com.sys.market.mapper.ItemMapper;
import com.sys.market.mapper.OfferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferMapper offerMapper;
    private final ItemMapper itemMapper;

    public Offer findOffer(Integer id) {
        return offerMapper.selectOfferById(id);
    }

    public List<Offer> findOfferList(OfferSearchCriteria criteria) {
        return offerMapper.selectOfferList(criteria);
    }

    public int findOfferCount(OfferSearchCriteria criteria) {
        return offerMapper.selectOfferCount(criteria);
    }

    public Integer saveOffer(Offer offerInfo) {
        offerMapper.insertOffer(offerInfo);
        return offerInfo.getId();
    }

    @Transactional
    public void acceptOffer(Offer offer) {
        Item itemInfo = new Item();
        itemInfo.setId(offer.getItemId());
        itemInfo.setStatus(ItemStatus.TRADE);
        itemMapper.updateItem(itemInfo);

        offerMapper.updateOfferIsAccepted(offer.getId(), true);
    }

    @Transactional
    public void cancelOffer(Offer offer) {
        // 수락한 오퍼를 취소하는 경우
        if(Boolean.TRUE.equals(offer.getIsAccepted())){
            Item itemInfo = new Item();
            itemInfo.setId(offer.getItemId());
            itemInfo.setStatus(ItemStatus.OPEN);
            itemMapper.updateItem(itemInfo);
        }
        offerMapper.updateOfferIsAccepted(offer.getId(), false);
    }

    @Transactional
    public void completeOffer(Offer offer) {
        Item itemInfo = new Item();
        itemInfo.setId(offer.getItemId());
        itemInfo.setStatus(ItemStatus.COMPLETED);
        itemMapper.updateItem(itemInfo);

        offerMapper.updateOfferComplete(offer.getItemId());
        offerMapper.updateOfferIsAccepted(offer.getId(), true);
    }
}
