package com.sys.market.service;

import com.sys.market.entity.Address;
import com.sys.market.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressMapper addressMapper;

    public List<Address> findAddressList(String dongName) {
        return addressMapper.selectAddressListByDongName(dongName);
    }

    public Address findAddress(String code) {
        return addressMapper.selectAddress(code);
    }
}
