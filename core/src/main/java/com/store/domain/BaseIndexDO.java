package com.store.domain;

import com.core.dao.RecordDO;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by laizy on 2017/6/9.
 */
@MappedSuperclass
public class BaseIndexDO extends RecordDO {
    private Integer index;

    @Column(name = "index")
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
