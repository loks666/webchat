package com.webchat.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 */
@MappedSuperclass
@Data
public class BaseEntity extends SimpleBaseEntity implements Serializable {

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    @PreUpdate
    public void preUpdate() {
        this.updateDate = new Date();
    }
}
