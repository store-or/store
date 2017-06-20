package com.store.domain.product;

import com.core.dao.IdDO;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * 产品详情时候展示
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "product_detail")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_product_detail")
public class ProductDetailDO extends IdDO {
    private String properties;
    private String[] propertyArr;
    private String detail;

    @Column(name = "properties")
    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Transient
    public String[] getPropertyArr() {
        return propertyArr;
    }

    public void setPropertyArr(String[] propertyArr) {
        this.propertyArr = propertyArr;
    }

    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void parse() {
        propertyArr = StringUtils.split(properties, "\r\n");
    }

}
