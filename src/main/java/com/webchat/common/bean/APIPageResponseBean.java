package com.webchat.common.bean;

import lombok.Data;

import java.util.List;

@Data
public class APIPageResponseBean<T> {

    private Boolean success;

    private Integer code;

    private String msg;

    private Long pageNo;

    private Long pageSize;

    private Long total;

    private Long pageTotal;

    private List<T> data;

    public APIPageResponseBean() {

    }

    public APIPageResponseBean error() {
        APIPageResponseBean responseBean = new APIPageResponseBean();
        responseBean.success = false;
        responseBean.code = 500;
        responseBean.msg = "ERROR";
        return responseBean;
    }

    public static APIPageResponseBean success(long pageNo, long pageSize, long total, List data) {
        APIPageResponseBean responseBean = new APIPageResponseBean();
        responseBean.success = true;
        responseBean.code = 200;
        responseBean.msg = "OK";
        responseBean.pageNo = pageNo;
        responseBean.pageSize = pageSize;
        responseBean.data = data;
        responseBean.total = total;
        responseBean.pageTotal = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        return responseBean;
    }
}
