package com.webchat.repository.dao;

import com.webchat.repository.entity.ChatMessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IChatMessDAO extends JpaSpecificationExecutor<ChatMessEntity>,
        JpaRepository<ChatMessEntity, Long> {

    @Query(value = "select distinct mess from ChatMessEntity mess where "
            + "(mess.sender = :sender or mess.receiver = :receiver) and "
            + "(mess.receiver = :sender or mess.sender = :receiver) order by mess.id desc")
    List<ChatMessEntity> findAllBySenderAndReceiver(String sender, String receiver);

    Page<ChatMessEntity> findAllByMessageLike(String message, Pageable pageable);
}