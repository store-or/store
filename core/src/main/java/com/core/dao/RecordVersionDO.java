package com.core.dao;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by laizy on 2016/12/9.
 */
@MappedSuperclass
public class RecordVersionDO extends RecordDO {
    private Long version;

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}