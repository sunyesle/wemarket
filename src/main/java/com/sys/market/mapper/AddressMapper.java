package com.sys.market.mapper;

import com.sys.market.entity.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> selectAddressListByDongName(String dongName);

    Address selectAddress(String code);
}
