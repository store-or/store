package com.core.dao;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-2-13
 * Time: 下午1:31
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class IdDO {

    protected Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}