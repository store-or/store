package com.core.dao;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 */
@MappedSuperclass
public class RecordDO extends IdDO {

    protected String createUser;
    protected String modifyUser;
    protected Long createTime;
    protected Long modifyTime;

    @Column(name = "update_time")
    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "create_user", updatable = false)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "update_user")
    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Column(name = "create_time", updatable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void copyModify(RecordDO record) {
        this.modifyTime = record.modifyTime;
        this.modifyUser = record.modifyUser;
    }

}
