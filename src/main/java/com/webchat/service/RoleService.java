package com.webchat.service;

import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @Author: by loks666 on GitHub
 * @Date: 11.3.22 1:38 上午
 */
@Slf4j
@Service
public class RoleService {

    @Autowired
    private UserService userService;

    /***
     * 校验用户是否拥有权限
     * @param userId   用户ID
     * @param roleCode 权限CODE
     * @return
     */
    public boolean hasRole(String userId, Integer roleCode) {
        if (StringUtils.isBlank(userId) || roleCode == null) {
            log.error("{} not has role: {}!", userId, roleCode);
            return false;
        }
        UserBaseResponseInfoVO userBaseResponseInfoVO = userService.getUserInfoByUserId(userId);
        Assert.isTrue(userBaseResponseInfoVO != null, "用户不存在");
        return roleCode.equals(userBaseResponseInfoVO.getRoleCode());
    }
}
