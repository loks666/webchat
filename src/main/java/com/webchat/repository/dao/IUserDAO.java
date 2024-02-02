package com.webchat.repository.dao;

import com.webchat.repository.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IUserDAO extends JpaSpecificationExecutor<UserEntity>, JpaRepository<UserEntity, Long> {

    UserEntity findByMobile(String mobile);

    UserEntity findByUserId(String userId);

    UserEntity findByUserName(String userName);

    Page<UserEntity> findAllByRoleCode(Integer roleCode, Pageable pageable);

    /***
     * 根据手机号+密码查询用户信息，登录场景
     * @param mobile
     * @param password
     * @return
     */
    UserEntity findByMobileAndPassword(String mobile, String password);
}
