package com.store.domain;

import com.store.domain.product.ProductDO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "recommend")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_recommend")
public class RecommendDO extends BaseIndexDO {
    @NotNull(message = "请选择产品")
    private ProductDO product;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public ProductDO getProduct() {
        return product;
    }

    public void setProduct(ProductDO product) {
        this.product = product;
    }
}
