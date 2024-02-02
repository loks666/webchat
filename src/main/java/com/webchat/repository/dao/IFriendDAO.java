package com.webchat.repository.dao;

import com.webchat.repository.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: by loks666 on GitHub
 * @Date: 2021-7-24 0024 3:36
 * @Description: 无描述信息
 */
@Repository
public interface IFriendDAO extends JpaSpecificationExecutor<FriendEntity>, JpaRepository<FriendEntity, Long> {

    FriendEntity findAllByUserIdAndFriendId(String userId, String friendId);

    Long countByFriendIdAndStatus(String friendId, Integer status);

    List<FriendEntity> findAllByFriendIdAndStatusOrderByApplyDateDesc(String friendId, Integer status);

    List<FriendEntity> findAllByUserIdAndStatusOrderByApplyDateDesc(String userId, Integer status);

    @Query("select f from FriendEntity f inner join UserEntity u on u.userId = f.friendId " +
            "where f.userId = :userId and f.status = :status and u.userName like :userName order by f.applyDate desc")
    List<FriendEntity> findAllByUserIdAndUserNameLikeAndStatusOrderByApplyDateDesc(String userId, String userName, Integer status);
}
