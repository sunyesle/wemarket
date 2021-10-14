package com.sys.market.service;

import com.sys.market.dto.criteria.Criteria;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.ListResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.dto.result.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    public enum CommonResponse {
        SUCCESS("성공하였습니다."),
        FAIL("실패하였습니다");

        String msg;

        CommonResponse(String msg){
            this.msg = msg;
        }

        public String getMsg() { return msg; }
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list){
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public <T> PageResult<T> getPageResult(List<T> list, Criteria criteria){
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setPage(criteria.getPage());
        result.setPageSize(criteria.getPageSize());
        result.setTotalCount(criteria.getTotalCount());
        return result;
    }

    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult(){
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setMsg(CommonResponse.FAIL.getMsg());
        return result;
    }

    public CommonResult getFailResult(String msg){
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅한다.
    private void setSuccessResult(CommonResult result){
        result.setSuccess(true);
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
