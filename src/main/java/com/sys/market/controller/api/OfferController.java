package com.sys.market.controller.api;

import com.sys.market.advice.exception.CAccessDeniedException;
import com.sys.market.advice.exception.CBadRequestException;
import com.sys.market.advice.exception.CConflictException;
import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.dto.IdInfo;
import com.sys.market.dto.criteria.OfferSearchCriteria;
import com.sys.market.entity.Item;
import com.sys.market.entity.Offer;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.enums.ItemStatus;
import com.sys.market.service.ItemService;
import com.sys.market.service.OfferService;
import com.sys.market.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;
    private final ResponseService responseService;
    private final ItemService itemService;

    @Operation(summary = "제안 조회")
    @GetMapping("/{id}")
    public SingleResult<Offer> offer(@PathVariable Integer id, HttpServletResponse response){
        Offer offer = offerService.findOffer(id);
        if(offer == null) response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return responseService.getSingleResult(offer);
    }

    @Operation(summary = "제안 리스트 조회")
    @GetMapping
    public PageResult<Offer> offerList(@ModelAttribute OfferSearchCriteria criteria, HttpServletResponse response){
        List<Offer> offerList = new ArrayList<>();

        int totalOfferCount = offerService.findOfferCount(criteria);
        if(totalOfferCount <= (criteria.getPage()-1) * criteria.getPageSize()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(offerList, criteria);
        }

        criteria.paging(totalOfferCount);
        offerList = offerService.findOfferList(criteria);

        return responseService.getPageResult(offerList,criteria);
    }

    @Operation(summary = "제안 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult offerSave(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                  @RequestBody @Valid OfferRequest offerRequest){
        // 존재하지 않는 아이템, 아이템의 상태가 OPEN 이 아님, 아이템의 판매자와 현재 사용자가 동일할 경우
        Item item = itemService.findItem(offerRequest.getItemId());
        if(item == null){
            throw new CBadRequestException("존재하지 않는 상품입니다.");
        }else if(item.getStatus() != ItemStatus.OPEN) {
            throw new CBadRequestException("판매중인 상품에만 제안할 수 있습니다.");
        }else if(item.getUserId().equals(user.getId())){
            throw new CBadRequestException("자신의 상품에는 제안할 수 없습니다.");
        }

        OfferSearchCriteria criteria = new OfferSearchCriteria();
        criteria.setItemId(offerRequest.getItemId());
        criteria.setBuyerId(user.getId());
        List<Offer> currentUserOfferList = offerService.findOfferList(criteria);
        for(Offer o : currentUserOfferList){
            // 취소 상태가 아닌 오퍼가 존재할경우
            if(o.getIsAccepted() == null || o.getIsAccepted()){
                throw new CConflictException("이미 해당 상품에 대기중인 제안이 있습니다.");
            }
        }

        Offer offerInfo = new Offer();
        offerInfo.setBuyerId(user.getId());
        offerInfo.setSellerId(item.getUserId());
        offerInfo.setItemId(offerRequest.getItemId());
        offerInfo.setPrice(offerRequest.getPrice());

        Integer offerId = offerService.saveOffer(offerInfo);
        return responseService.getSingleResult(new IdInfo<>(offerId));
    }

    @Operation(summary = "제안 수락")
    @PutMapping("/{id}/accept")
    public CommonResult acceptOffer(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                    @PathVariable Integer id) {
        // 존재하지 않는 오퍼, 오퍼가 OPEN 상태가 아닐 경우
        Offer offer = offerService.findOffer(id);
        if(offer == null){
            throw new CNotFoundException("존재하지 않는 제안입니다.");
        }else if(offer.getIsAccepted() != null){
            throw new CConflictException("이미 수락 또는 거절된 제안입니다.");
        }

        // OPEN 상태가 아니거나, 자신이 판매하는 아이템의 오퍼가 아닐 경우
        Item item = itemService.findItem(offer.getItemId());
        if(item == null){
            throw new CNotFoundException("존재하지 않는 상품입니다.");
        }else if(item.getStatus() != ItemStatus.OPEN){
            throw new CBadRequestException("판매중인 상품에만 제안할 수 있습니다.");
        }else if(!item.getUserId().equals(user.getId())){
            throw new CBadRequestException("자신이 판매하는 상품의 제안만 수락할 수 있습니다.");
        }

        offerService.acceptOffer(offer);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "상품 취소")
    @PutMapping("/{id}/cancel")
    public CommonResult cancelOffer(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                    @PathVariable Integer id) {
        // 존재하지 않는 오퍼, 현재 사용자가 seller 또는 buyer가 아님, 취소된 오퍼, 완료된 오퍼일 경우
        Offer offer = offerService.findOffer(id);
        if(offer == null){
            throw new CNotFoundException("존재하지 않는 제안입니다.");
        }else if(!(user.getId().equals(offer.getSellerId()) || user.getId().equals(offer.getBuyerId()))){
            throw new CAccessDeniedException("상품의 판매자 또는 구매자만 제안을 완료할 수 있습니다.");
        }else if(Boolean.FALSE.equals(offer.getIsAccepted())){
            throw new CConflictException("이미 취소된 제안입니다.");
        }else if(Boolean.TRUE.equals(offer.getIsCompleted())){
            throw new CConflictException("이미 완료된 제안입니다.");
        }

        offerService.cancelOffer(offer);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "제안 완료")
    @PutMapping("/{id}/complete")
    public CommonResult completeOffer(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                    @PathVariable Integer id) {
        // 존재하지 않는 오퍼, 현재 사용자가 seller 또는 buyer가 아님, 오퍼가 수락상태가 아닐 경우
        Offer offer = offerService.findOffer(id);
        if(offer == null){
            throw new CNotFoundException("존재하지 않는 제안입니다.");
        }else if(!(user.getId().equals(offer.getSellerId()) || user.getId().equals(offer.getBuyerId()))){
            throw new CAccessDeniedException("상품의 판매자 또는 구매자만 제안을 완료할 수 있습니다.");
        }else if((offer.getIsAccepted() == null || !offer.getIsAccepted())){
            throw new CBadRequestException("수락상태인 제안만 완료할 수 있습니다.");
        }

        // 존재하지 않는 아이템, 이미 거래완료 상태인 아이템일 경우
        Item item = itemService.findItem(offer.getItemId());
        if(item == null){
            throw new CNotFoundException("존재하지 않는 상품입니다.");
        }else if(item.getStatus() == ItemStatus.COMPLETED) {
            throw new CConflictException("이미 완료된 제안입니다.");
        }

        offerService.completeOffer(offer);
        return responseService.getSuccessResult();
    }

    @Getter
    @Setter
    public static class OfferRequest {
        @NotNull
        private Integer itemId;

        @NotNull(message = "가격을 입력해주세요.")
        @Min(value=0, message = "가격은 0~999999999 사이의 값만 입력할 수 있습니다.")
        @Max(value=999999999, message = "가격은 0~999999999 사이의 값만 입력할 수 있습니다.")
        private Integer price;
    }

}
