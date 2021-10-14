package com.sys.market.controller.api;

import com.sys.market.entity.Address;
import com.sys.market.dto.result.ListResult;
import com.sys.market.service.AddressService;
import com.sys.market.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value="/api/addresses")
public class AddressController {
    private final AddressService addressService;
    private final ResponseService responseService;

    @Operation(summary = "주소 리스트 조회")
    @GetMapping
    public ListResult<Address> addressList(@RequestParam @Size(min=2, message = "2자 이상 입력해주세요.") String dongName,
                                           HttpServletResponse response){
        List<Address> addressList = addressService.findAddressList(dongName);

        if(addressList.isEmpty()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getListResult(addressList);
        }

        return responseService.getListResult(addressList);
    }
}
