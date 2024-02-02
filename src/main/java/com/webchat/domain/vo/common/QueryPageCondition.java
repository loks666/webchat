package com.webchat.domain.vo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: by loks666 on GitHub
 * @Date: 2021-6-19 0019 17:54
 * @Description: 无描述信息
 */
@Data
@AllArgsConstructor
public class QueryPageCondition {

    private Integer pageNo = 1;

    private Integer pageSize = 10;
}
