package com.sys.market.dto.criteria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Criteria {
    private Integer pageSize = 10; // 한 페이지에 표시할 양 (request로 받아옴)
    private Integer page = 1; // 현재 페이지 (request로 받아옴)

    @Schema(hidden = true)
    private Integer totalCount; // 전체 글 개수 (db에서 받아옴)

    @Schema(hidden = true)
    private Integer startRow = 1; // 시작 행 번호
    @Schema(hidden = true)
    private Integer endRow; // 종료 행 번호
    @Schema(hidden = true)
    private Integer totalPage; // 전체 페이지 수

    public void setPageSize(Integer pageSize) {
        if( pageSize == null || pageSize < 1) pageSize = 10;
        if(pageSize > 100) pageSize = 100;
        this.pageSize = pageSize;
    }

    public void setPage(Integer page) {
        if( page==null || page < 1) page = 1;
        this.page = page;
    }

    public void paging(Integer totalRow){
        this.totalCount = totalRow;

        totalPage = totalRow/ pageSize;
        if(totalRow % pageSize > 0) totalPage++; //나머지가 있다면 전체페이지수 +1

        startRow = ((page-1)* pageSize) +1;
        endRow = startRow + pageSize -1;
    }
}
