package com.webchat.service;

import com.webchat.common.enums.FriendStatusEnum;
import com.webchat.common.exception.BusinessException;
import com.webchat.domain.vo.response.FriendApplyUserVO;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.repository.dao.IFriendDAO;
import com.webchat.repository.entity.FriendEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 好友关注服务
 */
@Service
public class FriendService {

    @Autowired
    private IFriendDAO friendDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    /**
     * 申请添加好友
     * @param userId
     * @param friendId
     * @return
     */
    public boolean applyAddFriend(String userId, String friendId) {
        Assert.isTrue(!userId.equals(friendId), "申请失败：不能添加自己好友呃～");
        UserBaseResponseInfoVO friend = userService.getUserInfoByUserId(friendId);
        Assert.isTrue(friend != null, "申请失败：好友不存在！");
        FriendEntity friendEntity = friendDAO.findAllByUserIdAndFriendId(userId, friendId);
        if (friendEntity == null) {
            friendEntity = new FriendEntity();
            friendEntity.setUserId(userId);
            friendEntity.setFriendId(friendId);
            friendEntity.setApplyDate(new Date());
            friendEntity.setStatus(FriendStatusEnum.APPLY.getStatus());
        } else {
            if (FriendStatusEnum.PASS.getStatus().equals(friendEntity.getStatus())) {
                throw new BusinessException("已经是好友了,不需要再次申请！");
            } else  {
                // 再次申请，修改申请时间，但保持一条记录
                friendEntity.setApplyDate(new Date());
                friendEntity.setHandleDate(null);
            }
        }
        friendDAO.save(friendEntity);
        return true;
    }

    /**
     * 同意申请好友
     * @param userId
     * @param id
     */
    @Transactional
    public void applyPass(String userId, Long id) {
        FriendEntity friendEntity = friendDAO.findById(id).orElse(null);
        Assert.isTrue(friendEntity != null, "处理失败，申请记录不存在！");
        Assert.isTrue(friendEntity.getFriendId().equals(userId), "处理失败，无权限");
        friendEntity.setStatus(FriendStatusEnum.PASS.getStatus());
        friendEntity.setHandleDate(new Date());
        friendDAO.save(friendEntity);
        FriendEntity meFriend = friendDAO.findAllByUserIdAndFriendId(friendEntity.getFriendId(), friendEntity.getUserId());
        if (meFriend == null) {
            meFriend = new FriendEntity();
            meFriend.setUserId(userId);
            meFriend.setFriendId(friendEntity.getUserId());
            meFriend.setApplyDate(friendEntity.getApplyDate());
            meFriend.setHandleDate(new Date());
            meFriend.setStatus(FriendStatusEnum.PASS.getStatus());
        } else {
            meFriend.setStatus(FriendStatusEnum.PASS.getStatus());
            meFriend.setHandleDate(new Date());
        }
        friendDAO.save(meFriend);
    }

    /**
     * 拒绝申请
     * @param userId
     * @param id
     */
    @Transactional
    public void applyRefuse(String userId, Long id) {
        FriendEntity friendEntity = friendDAO.findById(id).orElse(null);
        Assert.isTrue(friendEntity != null, "处理失败，申请记录不存在！");
        Assert.isTrue(friendEntity.getFriendId().equals(userId), "处理失败，无权限");
        friendEntity.setStatus(FriendStatusEnum.REFUSE.getStatus());
        friendEntity.setHandleDate(new Date());
        friendDAO.save(friendEntity);
    }

    /**
     * 查询当前用户待处理的申请数量
     * @param userId
     * @return
     */
    public Long countUnHandleApply(String userId) {
        Long count = friendDAO.countByFriendIdAndStatus(userId, FriendStatusEnum.APPLY.getStatus());
        return count == null ? 0L : count;
    }

    public List<FriendApplyUserVO> listUnHandleApplyUsers(String userId) {
        List<FriendEntity> friendEntities =
                friendDAO.findAllByFriendIdAndStatusOrderByApplyDateDesc(userId, FriendStatusEnum.APPLY.getStatus());
        return convertApplyUserList(friendEntities);
    }

    /**
     * 查询用户的好友列表
     * @param userId
     * @return
     */
    public List<FriendApplyUserVO> listFriendUsers(String userId, String userName) {
        if (StringUtils.isBlank(userName)) {
            List<FriendEntity> friendEntities =
                    friendDAO.findAllByUserIdAndStatusOrderByApplyDateDesc(userId, FriendStatusEnum.PASS.getStatus());
            return convertFriendUserList(friendEntities);
        }
        List<FriendEntity> friendEntities =
        friendDAO.findAllByUserIdAndUserNameLikeAndStatusOrderByApplyDateDesc(userId, "%"+userName+"%",
                FriendStatusEnum.PASS.getStatus());
        return convertFriendUserList(friendEntities);
    }

    private List<FriendApplyUserVO> convertFriendUserList( List<FriendEntity> friendEntities) {
        if (CollectionUtils.isEmpty(friendEntities)) {
            return Collections.emptyList();
        }
        List<String> userIdList = friendEntities.stream().map(FriendEntity::getFriendId).collect(Collectors.toList());
        Map<String, UserBaseResponseInfoVO> userMap = userService.batchGetUserInfoFromCache(userIdList);
        return friendEntities.stream().map(f -> {
            return new FriendApplyUserVO(f.getId(), f.getApplyDate().getTime(), userMap.get(f.getFriendId()));
        }).collect(Collectors.toList());
    }

    private List<FriendApplyUserVO> convertApplyUserList( List<FriendEntity> friendEntities) {
        if (CollectionUtils.isEmpty(friendEntities)) {
            return Collections.emptyList();
        }
        List<String> userIdList = friendEntities.stream().map(FriendEntity::getUserId).collect(Collectors.toList());
        Map<String, UserBaseResponseInfoVO> userMap = userService.batchGetUserInfoFromCache(userIdList);
        return friendEntities.stream().map(f -> {
            return new FriendApplyUserVO(f.getId(), f.getApplyDate().getTime(), userMap.get(f.getUserId()));
        }).collect(Collectors.toList());
    }
}
