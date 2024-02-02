package com.webchat.domain.vo.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: by loks666 on GitHub
 * @Date: 2021-6-19 0019 17:53
 * @Description: 无描述信息
 */
@Data
public class QueryOrderByCondition {

    /**
     * 排序字段
     */
    private String field;

    /**
     * 排序类型：ASC / DESC
     */
    private String orderType;

    public QueryOrderByCondition of(String field, String orderType) {
        this.field = field;
        this.orderType = orderType;
        return this;
    }

    public List<QueryOrderByCondition> addAll(QueryOrderByCondition ... orders) {
        List<QueryOrderByCondition> queryOrderByConditionList = new ArrayList<>();
        for (QueryOrderByCondition order : orders) {
            queryOrderByConditionList.add(order);
        }
        return queryOrderByConditionList;
    }

}
