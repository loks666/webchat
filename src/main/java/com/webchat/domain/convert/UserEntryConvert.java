package com.webchat.domain.convert;

import com.webchat.common.util.CommonUtils;
import com.webchat.common.util.DateUtils;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.domain.vo.response.UserSafeResponseInfoVO;
import com.webchat.repository.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class UserEntryConvert {


    public static UserBaseResponseInfoVO convertBaseVo(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserBaseResponseInfoVO vo = new UserBaseResponseInfoVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setMobile(CommonUtils.mobileEncrypt(entity.getMobile()));
        return vo;
    }

    public static UserSafeResponseInfoVO convertSafeVo(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserSafeResponseInfoVO vo = new UserSafeResponseInfoVO();
        BeanUtils.copyProperties(entity, vo);
        if (entity.getCreateDate() != null) {
            vo.setRegistryTime(entity.getCreateDate().getTime());
            vo.setRegistryTimeStr(DateUtils.getDate2String(entity.getCreateDate()));
        }
        return vo;
    }
}
