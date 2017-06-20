package com.store.domain.product;

import com.google.common.collect.Lists;
import com.store.domain.ClassifyDO;
import com.core.dao.RecordDO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用于列表展示，省得加载太多属性，导致变慢
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "product")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_product")
public class ProductDO extends RecordDO {
    public static final String CLASSIFY_ID_SPLIT = ",";
    public static final String PICTURE_SPLIT = ",";
    @NotBlank(message = "产品名称不能为空")
    private String name;
    @NotBlank(message = "封面不能为空")
    private String cover;
    @NotBlank(message = "请选择分类")
    private String classifyIds;  // 保存多个的分类id，格式,100,100,
    private List<ClassifyDO> classifies;
    private String introduction;
    private String tag;      // Tag枚举
    private Long topTime;
    private Integer status;
    private String link;
    private String pictures;  // 多张截图，以,隔开
    @NotEmpty(message = "请添加产品图片")
    @Size(max = 4, message = "最多4张图片")
    private List<String> pictureList; // 用于前台交互需要
    private String description;
    private ProductDetailDO detailDO;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "cover")
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(name = "classify_ids")
    public String getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(String classifyIds) {
        this.classifyIds = classifyIds;
    }

    @ManyToMany
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "product_classify", joinColumns = {@JoinColumn(name="product_id")}, inverseJoinColumns={@JoinColumn(name="classify_id")})
    public List<ClassifyDO> getClassifies() {
        return classifies;
    }

    public void setClassifies(List<ClassifyDO> classifies) {
        this.classifies = classifies;
    }

    @Column(name = "introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(name = "tag")
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Column(name = "top_time", updatable = false)
    public Long getTopTime() {
        return topTime;
    }

    public void setTopTime(Long topTime) {
        this.topTime = topTime;
    }

    @Column(name = "status", updatable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "pictures")
    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    @Transient
    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "detail_id")
    public ProductDetailDO getDetailDO() {
        return detailDO;
    }

    public void setDetailDO(ProductDetailDO detailDO) {
        this.detailDO = detailDO;
    }

    // 将数据库的一些数据转化为前台需要
    public void parse() {
        String[] pictureArr = StringUtils.split(pictures, PICTURE_SPLIT);
        if (!ArrayUtils.isEmpty(pictureArr)) {
            pictureList = Lists.newArrayList(pictureArr);
        }
    }

    // 将前台数据转化为数据库保存格式
    public void transform() {
        classifyIds = CLASSIFY_ID_SPLIT + classifyIds + CLASSIFY_ID_SPLIT;
        pictures = StringUtils.join(pictureList, PICTURE_SPLIT);
    }
}
