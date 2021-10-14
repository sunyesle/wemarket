package com.sys.market.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResult<T> extends ListResult<T>{
    private Integer page;
    private Integer pageSize;
    private Integer totalCount;
}
