package com.store.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by laizy on 2017/6/9.
 */
@Entity
@Table(name = "menu")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_menu")
public class MenuDO extends BaseIndexDO {
    private String type;
    @NotBlank(message = "菜单名称不能为空")
    private String name;
    @NotBlank(message = "内容不能为空")
    private String currentContent;
    private String publishContent;
    private String currentVersion;
    private String publishVersion;

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "current_content")
    public String getCurrentContent() {
        return currentContent;
    }

    public void setCurrentContent(String currentContent) {
        this.currentContent = currentContent;
    }

    @Column(name = "publish_content")
    public String getPublishContent() {
        return publishContent;
    }

    public void setPublishContent(String publishContent) {
        this.publishContent = publishContent;
    }

    @Column(name = "current_version")
    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Column(name = "publish_version")
    public String getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(String publishVersion) {
        this.publishVersion = publishVersion;
    }
}
