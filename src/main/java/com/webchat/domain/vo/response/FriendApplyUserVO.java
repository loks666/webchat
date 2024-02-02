package com.webchat.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author by loks666 on GitHub
 * @webSite https://www.coderutil.com
 * @Date 2022/12/10 20:33
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendApplyUserVO {

    private Long applyId;

    private Long applyTime;

    private UserBaseResponseInfoVO applyUser;
}
