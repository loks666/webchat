package com.webchat.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 */
@MappedSuperclass
@Data
public class SimpleBaseEntity implements Serializable {

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Version
    @Column(name = "version")
    private Integer version;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (this.createDate == null) {
            this.createDate = now;
        }
    }
}
