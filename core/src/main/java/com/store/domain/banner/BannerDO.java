package com.store.domain.banner;

import com.store.domain.BaseIndexDO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "banner")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_banner")
public class BannerDO extends BaseIndexDO {
    @NotBlank(message = "名称不能为空")
    private String name;
    private String type;
    @NotBlank(message = "海报不能为空")
    private String poster;
    private String link;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "poster")
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
