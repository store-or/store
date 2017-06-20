package com.store.domain;

import com.store.domain.product.ProductDO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "classify")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_classify")
public class ClassifyDO extends BaseIndexDO {
    @NotBlank(message = "名称不能为空")
    private String name;
    private List<ProductDO> products;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public List<ProductDO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDO> products) {
        this.products = products;
    }
}
